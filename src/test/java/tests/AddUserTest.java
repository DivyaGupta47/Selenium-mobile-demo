package tests;

import org.testng.annotations.Test;
import org.testng.Assert;
import pages.LoginPage;
import pages.AddUserPage;
import base.BaseTest;

public class AddUserTest extends BaseTest {

    LoginPage loginPage;
    AddUserPage addUserPage;
    String userName = "reportautomation@yopmail.com";
    String password = "KestrelPro@123";

    public void loginUI() {
        loginPage = new LoginPage(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
    }

    @Test
    public void testAddUser() {
        // Step 1: Login
        loginUI();

        // Step 2: Navigate to Settings and Add Member
        addUserPage = new AddUserPage(driver);
        addUserPage.Settings();
        addUserPage.addMember();

        // Step 3: Fill the Add User form
        addUserPage.enterFirstName("Test");
        addUserPage.enterLastName("User");
        addUserPage.enterEmail("testuser" + System.currentTimeMillis() + "@yopmail.com");
        addUserPage.enterPhone("1234567890");
        addUserPage.selectClientAdminRole();
        addUserPage.selectTimezone("(GMT+5:30) Kolkata, India");

        // Step 4: Submit the form
        addUserPage.clickAddButton();

        System.out.println("click");

        // Step 5: Assert user added (adjust as per your app's confirmation message or user list)
        //Assert.assertTrue(addUserPage.isUserAdded("Test User"), "User was not added successfully.");
    }
}