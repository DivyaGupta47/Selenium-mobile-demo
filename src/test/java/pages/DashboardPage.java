package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {
    WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);  // Initialize Page Factory
    }
    
    @FindBy(xpath = "//div[text()='Total Orders']/parent::li/h2")
    WebElement totalOrdersCount;
    
    @FindBy(xpath = "//div[text()='On Time Orders']/parent::li/h2")
    WebElement onTimeOrdersCount;
    
    @FindBy(xpath = "//div[text()='Completed Orders']/parent::li/h2")
    WebElement completedOrdersCount;
    
    @FindBy(xpath = "//div[text()='Queued Orders']/parent::li/h2")
    WebElement queuedOrdersCount;
    
 
    public int getTotalOrdersCount() throws InterruptedException {
        return Integer.parseInt(totalOrdersCount.getText());
    }

    public int getCompletedOrdersCount() throws InterruptedException {
        return Integer.parseInt(completedOrdersCount.getText());
    }

    public int getOnTimeOrdersCount() throws InterruptedException {
        return Integer.parseInt(onTimeOrdersCount.getText());
    }

    public int getQueuedOrdersCount() throws InterruptedException {
        return Integer.parseInt(queuedOrdersCount.getText());
    }


   /* public int getTotalOrdersCount() throws InterruptedException {
    	 Thread.sleep(5000); 
        WebElement totalOrdersElement = totalOrdersCount;
        return Integer.parseInt(totalOrdersElement.getText());
    }*/
}