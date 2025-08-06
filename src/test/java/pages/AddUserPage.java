package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddUserPage {
    WebDriver driver;
    WebDriverWait wait;

    @FindBy(xpath = "//a[@href='/settings']//span[text()='Settings']")
    WebElement settingsButton;

    @FindBy(xpath = "//button[contains(., 'Add Member')]")
    WebElement addMemberButton;

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "phone")
    private WebElement phoneInput;

    @FindBy(xpath = "//button[contains(., 'Select Role')]")
    WebElement cickselectrole;

    @FindBy(xpath = "//span[text()='Client Admin']")
    WebElement selectclientadmin;

    @FindBy(id = "react-select-2-input")
    private WebElement timezoneInput;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    private WebElement addButton;

    public AddUserPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void Settings() {
        wait.until(ExpectedConditions.elementToBeClickable(settingsButton)).click();
    }

    public void addMember() {
        wait.until(ExpectedConditions.elementToBeClickable(addMemberButton)).click();
    }

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput)).clear();
        firstNameInput.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        wait.until(ExpectedConditions.visibilityOf(lastNameInput)).clear();
        lastNameInput.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput)).clear();
        emailInput.sendKeys(email);
    }

    public void enterPhone(String phone) {
        wait.until(ExpectedConditions.visibilityOf(phoneInput)).clear();
        phoneInput.sendKeys(phone);
    }

    public void selectClientAdminRole() {
        cickselectrole.click();
        selectclientadmin.click();
    }

    public void selectTimezone(String timezone) {
        wait.until(ExpectedConditions.visibilityOf(timezoneInput)).sendKeys(timezone);
        timezoneInput.sendKeys(Keys.ENTER);
    }

    public void clickAddButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }
}