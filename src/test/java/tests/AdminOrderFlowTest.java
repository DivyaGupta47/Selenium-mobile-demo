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
import apis.OrderAPI;
import apis.OrderVerificationAPI;
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
	
    String customerName = "JANN1 Automation";
    String assigneeEmail = "TestAutomation206@yopmail.com";
    String firstName = "Test";
    String lastName = "Automation";
    String roleAssociate = "EMPLOYEE";
    String phone = "0123567891";
    String timeZone="(GMT+5:30) Kolkata, India";
    
    String userId;

    Integer orderId;
    Integer stageIdToUpdate;
    List<Map<String, Object>> filteredStages1;

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
}
