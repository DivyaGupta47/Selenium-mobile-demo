package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;

    @BeforeClass
    public void setup() {
    	WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  //  Required for GitHub Actions
        options.addArguments("--no-sandbox");  //  Prevents crashing in CI
        options.addArguments("--disable-dev-shm-usage");  //  Prevents memory issues
        options.addArguments("--disable-gpu");  //  Optional but safe
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        //driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://freetrial-mf.kestrelpro.ai/");
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testLogin() throws InterruptedException {
        loginPage.signIn("divyaadmin@yopmail.com", "KestrelPro@123");
        Thread.sleep(5000);
        System.out.println("Login Successfully");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
