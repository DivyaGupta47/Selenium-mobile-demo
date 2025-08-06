package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateOrderPage;
import pages.LoginPage;
import pages.MyTaskFlowAssigneePage;
import pages.ProductFlowQueuedPage;
/**
 * Admin-AsigneeOrderFlowTest performs a complete end-to-end test flow for the Admin-Asignee user using UI automation.
 *
 * Flow Overview:
 * - Admin logs in and initiates a session
 * - Creates a new order
 * - Update TAT
 * - Assign Asignee
 * - Added Remarks
 * - AddedAttachment
 * - Admin logout
 * - Assignee Login
 * - Assignee complete the all stages 2 to 7
 * - Verify on the complete task
 * 
 * Author: QA@47Billion
 */
public class CreateOrderTest extends BaseTest {

	LoginPage loginPage;

	@Test(priority = 1)
	public void testFillNewOrderAndCancelviaAdmin() throws InterruptedException {

		loginPage = new LoginPage(driver);
		loginPage.enterUsername("divyaadmin@yopmail.com");
		loginPage.enterPassword("KestrelPro@123");
		loginPage.clickSignIn();
		CreateOrderPage createOrder = new CreateOrderPage(driver);

		int beforeCount = createOrder.getTotalOrdersCount();
		System.out.println("Total Orders before: " + beforeCount);

		createOrder.clickNewOrder();
		createOrder.fillNewOrderFormAndCancel();

		int afterCount = createOrder.getTotalOrdersCount();
		System.out.println("Total Orders after: " + afterCount);

		Assert.assertEquals(afterCount, beforeCount + 1, "Total orders count did not increase by 1");

		ProductFlowQueuedPage productFlowQueuedPage = new ProductFlowQueuedPage(driver);
		productFlowQueuedPage.completeOrderQueuedFlow();
		
		loginPage.signOutAdmin();
	}

	@Test(priority = 2)
	public void assigneeFlow() throws InterruptedException {
		loginPage = new LoginPage(driver);
		loginPage.enterUsername("divya_a@yopmail.com");
		loginPage.enterPassword("KestrelPro@123");
		loginPage.clickSignIn();

		System.out.println("Login As Asignee");
		Thread.sleep(3000);
		MyTaskFlowAssigneePage myTaskFlowAssigneePage = new MyTaskFlowAssigneePage(driver);
		myTaskFlowAssigneePage.completeOrderTillStage7AndLogout();
	}

}
