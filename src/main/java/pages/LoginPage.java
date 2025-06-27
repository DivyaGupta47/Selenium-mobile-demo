package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;

    @FindBy(name = "identifier")
    WebElement usernameField;

    @FindBy(name = "password")
    WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' and text()='Sign in with password']")
    WebElement signInButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void signIn(String username, String password) throws InterruptedException {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        Thread.sleep(500);
        signInButton.click();
    }   
}
