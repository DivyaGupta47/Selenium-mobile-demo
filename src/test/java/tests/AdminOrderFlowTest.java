package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import apis.AssigneeAPI;
import apis.AttachmentAPI;
import apis.OrderAPI;
import apis.OrderVerificationAPI;
import apis.RemarkAPI;
import apis.SplitAPI;
import apis.StageAPI;
import apis.StageUpdateTATAPI;
import apis.StatusAPI;
import apis.UserAPI;
import io.restassured.response.Response;
import listeners.ExtentTestNGListener;
import utils.Config;
import utils.LoginUtil;
/**
 * AdminOrderFlowTest performs a complete end-to-end test flow for the Admin user using API automation.
 *
 * Flow Overview:
 * - Admin logs in and initiates a session
 * - Creates a new order
 * 
 * Author: QA@47Billion
 */

@Listeners(ExtentTestNGListener.class)
public class AdminOrderFlowTest {

	String adminEmail = "automation@yopmail.com";
	String adminPassword = "KestrelPro@123";
	
    String customerName = "Manveer Automation"+ System.currentTimeMillis();
    //String assigneeEmail = "TestAutomation270@yopmail.com";
    //String firstName = "Test";
    String lastName = "Automation";
    String roleAssociate = "EMPLOYEE";
    String phone = "0123567891";
    String timeZone="(GMT+5:30) Kolkata, India";
    
    String userId;

    Integer orderId;
    Integer stageIdToUpdate;
    List<Map<String, Object>> filteredStages1;

    
    String firstName = "TestUser_" + System.currentTimeMillis(); // Generates a unique name like TestUser_1720090812345
    String assigneeEmail = firstName.toLowerCase() + "@yopmail.com"; // Ensure email is also unique
    
    @BeforeClass
    public void setupSession() {
        String token = LoginUtil.performLogin(adminEmail,adminPassword);
        Config.setSessionToken(token);
        System.out.println("Login Successfully with Admin Email " +adminEmail);
    }
    
    @Test
    public void createOrder() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
    	//String payload = PayloadUtil.loadJsonAsString("payloads/create_order.json");
        String payload = "{"
            + "\"requestDate\":\"2025-06-17T09:00:00.000Z\","
            + "\"consigneeDetails\":\"Indore Warehouse A1\","
            + "\"palletTier\":\"2\","
            + "\"palletType\":\"Euro\","
            + "\"promiseDay\":\"2025-06-20T09:00:00.000Z\","
            + "\"grade\":\"A+\","
            + "\"salesCategory\":\"Industrial\","
            + "\"packagingType\":\"Shrink Wrap\","
            + "\"salesOrderLineNumber\":\"SO/9821\","
            + "\"destination\":\"Indore Distribution Center\","
            + "\"noOfRolls\":25,"
            + "\"od\":45,"
            + "\"coreId\":\"76\","
            + "\"width\":120,"
            + "\"length\":200,"
            + "\"soNumber\":\"ORD123456\","
            + "\"filmType\":\"LDPE\","
            + "\"singleRollW\":50,"
            + "\"soDate\":\"2025-06-17T09:00:00.000Z\","
            + "\"umo\":\"Kg\","
            + "\"soQuantity\":1000,"
            + "\"region\":\"Madhya Pradesh\","
            + "\"customerCode\":\"CUST7890\","
            + "\"customerOrganizationName\":\"Kestrel Industries Pvt. Ltd.\","
            + "\"customerName\":\"" + customerName + "\""
            + "}";

        Response createResponse = OrderAPI.createOrder(payload);
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Order creation failed!");
        System.out.println("Create Status Code: " + createResponse.getStatusCode());
        System.out.println("Create Customer Response:\n" + createResponse.getBody().asString());
        orderId = OrderVerificationAPI.getOrderIdByCustomerName(customerName);
        Assert.assertNotNull(orderId);
        System.out.println("Order ID: " + orderId); 
        System.out.println("Customer found in queue. Customer Name: " + customerName);
        Thread.sleep(2000);
    }
    
    @Test(dependsOnMethods = "createOrder")
    public void splitOrder() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Response splitOrderResponse = SplitAPI.splitOrder(orderId, false);
        int statusCode = splitOrderResponse.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 201, "Split failed!");
    }

    @Test(dependsOnMethods = "splitOrder")
    public void updateTAT() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        List<Map<String, Object>> stages = StageAPI.getStageList(orderId);
        Map<String, Object> matchedStage = stages.stream()
            .filter(stage -> ((Number) stage.get("sequence")).intValue() == 2)
            .findFirst().orElse(null);

        Assert.assertNotNull(matchedStage, "Stage with sequence 2 not found!");
        stageIdToUpdate = ((Number) matchedStage.get("id")).intValue();

        Response updateResponse = StageUpdateTATAPI.updateTAT(stageIdToUpdate, orderId, 2, 4);
        System.out.println("TAT Update Status Code: " + updateResponse.getStatusCode());
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "Failed to update TAT!");
        Thread.sleep(2000);
    }

    @Test(dependsOnMethods = "updateTAT")
    public void addAndAssignUsers() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Response userResponse = UserAPI.createUserAssociate(assigneeEmail, firstName, lastName, roleAssociate, phone);
        System.out.println("Assignee Name : " +firstName +"Last Name : " +lastName);
        System.out.println("Assignee Email : " +assigneeEmail);
        Assert.assertEquals(userResponse.getStatusCode(), 200, "User creation failed!");

        userId = userResponse.jsonPath().getString("identity_id");
        System.out.println("New Member User ID: " + userId);
        List<Map<String, Object>> allStages = StageAPI.getStageList(orderId);
        filteredStages1 = allStages.stream()
            .filter(stage -> {
                int seq = ((Number) stage.get("sequence")).intValue();
                return seq >= 2 && seq <= 7;
            }).toList();

        int assigneeNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageIdToAssign = ((Number) stage.get("id")).intValue();
            Response resp = AssigneeAPI.assignStageToOrder(orderId, stageIdToAssign, List.of(userId));
            Assert.assertEquals(resp.getStatusCode(), 200);
            System.out.println("Assigned user to stage: " + assigneeNumber + ", Email: " + assigneeEmail);
            assigneeNumber++;
        }
    }

    @Test(dependsOnMethods = "addAndAssignUsers")
    public void addRemarks() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int remarkNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Remark from admin to Stage " + remarkNumber;
            Response response = RemarkAPI.addRemark(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            System.out.println("Added remark from admin to stage: " + remarkNumber);
            remarkNumber++;
        }
    }

    @Test(dependsOnMethods = "addRemarks")
    public void addAttachments() {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int attachmentNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            String comment = "Test Attachment from admin to Stage " + attachmentNumber;
            Response response = AttachmentAPI.addAttachment(orderId, stageId, comment);
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201);
            System.out.println("Added attachment from admin to stage: " + attachmentNumber);
            attachmentNumber++;
        }
    }

    @Test(dependsOnMethods = "addAttachments")
    public void completeStages() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        int completeNumber = 2;
        for (Map<String, Object> stage : filteredStages1) {
            int stageId = ((Number) stage.get("id")).intValue();
            Response response = StatusAPI.completeStage(orderId, stageId);
            Assert.assertEquals(response.getStatusCode(), 200);
            System.out.println("Completed from admin to stage: " + completeNumber);
            completeNumber++;
        }
        Thread.sleep(2000);
    }

    @Test(dependsOnMethods = "completeStages")
    public void verifyCompletion() throws InterruptedException {
    	ExtentTest test = ExtentTestNGListener.getTest();
        Integer orderIdCompleted = OrderVerificationAPI.getOrderIdByCustomerNameCompleted(customerName);
        Assert.assertNotNull(orderIdCompleted, "Order not found in COMPLETED list for customer: " + customerName);
        System.out.println("Order found in completed. Customer Name: " + customerName);
        
        Thread.sleep(2000);
        
        //Response countResponse = DashboardAPI.getDashboardCounts();
        //System.out.println("Final Dashboard Counts: " + countResponse.asPrettyString());
    }
    
   
	@Test(dependsOnMethods = {"addAndAssignUsers", "verifyCompletion"})
	public void updateUser() {
		ExtentTest test = ExtentTestNGListener.getTest();
	    String identityId = userId; // Or get it from user creation
	    Response response = UserAPI.updateUser(identityId, firstName, "AutomationUpdate", phone, timeZone);

	    Assert.assertEquals(response.getStatusCode(), 200, "User update failed!");
	    boolean isUpdated = response.jsonPath().getBoolean("is_updated");
	    Assert.assertTrue(isUpdated, "Update flag not true!");

	    System.out.println("User updated successfully. Identity ID: " + identityId);
	    System.out.println("Updated Response for user" +response.asPrettyString());
	}
	
	

}
