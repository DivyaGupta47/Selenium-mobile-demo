package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.DashboardPage;
import pages.LoginPage;

public class DashboardTest extends BaseTest {

    LoginPage loginPage;
    String userName = "divyaadmin@yopmail.com";
    String password = "KestrelPro@123";

    public void login()    
    {
    	loginPage = new LoginPage(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
    }
    
    @Test
    public void testDashboardCountsBefore() throws InterruptedException {
        login();
        DashboardPage dashboard = new DashboardPage(driver);
        Thread.sleep(5000);
        // Capture before counts
        int totalBefore = dashboard.getTotalOrdersCount();
        int completedBefore = dashboard.getCompletedOrdersCount();
        int onTimeBefore = dashboard.getOnTimeOrdersCount();
        int queuedBefore = dashboard.getQueuedOrdersCount();

        System.out.println("===== Dashboard Counts BEFORE Order Flow =====");
        System.out.println("Total Orders: " + totalBefore);
        System.out.println("Completed Orders: " + completedBefore);
        System.out.println("On Time Orders: " + onTimeBefore);
        System.out.println("Queued Orders: " + queuedBefore);;

        loginPage.signOutAdmin();
    }
    
    @Test
    public void testDashboardCountsAfter() throws InterruptedException {
    	login();
        DashboardPage dashboard = new DashboardPage(driver);
        Thread.sleep(5000);
        // Capture after counts
        int totalAfter = dashboard.getTotalOrdersCount();
        int completedAfter = dashboard.getCompletedOrdersCount();
        int onTimeAfter = dashboard.getOnTimeOrdersCount();
        int queuedAfter = dashboard.getQueuedOrdersCount();

        System.out.println("===== Dashboard Counts AFTER Order Flow =====");
        System.out.println("Total Orders: " + totalAfter);
        System.out.println("Completed Orders: " + completedAfter);
        System.out.println("On Time Orders: " + onTimeAfter);
        System.out.println("Queued Orders: " + queuedAfter);

        loginPage.signOutAdmin();
    }
    
    
}
