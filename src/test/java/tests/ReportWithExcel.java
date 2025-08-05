package tests;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import pages.ReportValidationPage;
import utils.ExcelDownloader;
import utils.ExcelReader;
import utils.LoginUtil;
import utils.Config;

public class ReportWithExcel extends BaseTest {

    LoginPage loginPage;
    String userName = "reportautomation@yopmail.com";
    String password = "KestrelPro@123";

    public void loginUI() {
        System.out.println("\n=========================================================");
        System.out.println("Starting Flow: REPORT ASSERTION FLOW TEST");
        System.out.println("=========================================================\n");

        loginPage = new LoginPage(driver);
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
    }

    @Test
    public void compareReportUIAndExcelData() throws InterruptedException {
        // Step 1: UI login first
        loginUI();

        // Step 2: Fetch UI data
        String customer = "Vasu Automation";
        ReportValidationPage page = new ReportValidationPage(driver);
        Map<String, String> uiData = page.getUIReportData(customer);

        // Step 3: Fresh API login (so token is valid)
        String sessionToken = LoginUtil.performLogin(userName, password);
        Config.setSessionToken(sessionToken);

        // Step 4: Download Excel via API
        String apiUrl = "https://freetrial-mf.kestrelpro.ai/api/v1/report/export"
                + "?&searchKeys=customerName"
                + "&searchKeys=status"
                + "&searchKeys=startDate"
                + "&searchKeys=endDate"
                + "&searchValues=" + customer.replace(" ", "%20")
                + "&searchValues=COMPLETED%20ON%20TIME"
                + "&searchValues=2025-04-01"
                + "&searchValues=2025-08-04";

        File excelFile = ExcelDownloader.downloadExcelFromAPI(apiUrl);
        System.out.println("Report downloded via API : "+excelFile);

        // Step 5: Read Excel data
        Map<String, String> excelData = ExcelReader.getCustomerDataExcel(excelFile, customer);

        // Step 6: Compare UI vs Excel data
        List<String> skipFields = Arrays.asList("Current Stage", "Organization Name");

        for (String key : excelData.keySet()) {
            if (skipFields.contains(key)) continue;

            String expected = excelData.get(key);
            String actual = uiData.getOrDefault(key, "");

            System.out.println("Comparing field: " + key + " | Expected: " + expected + " | Actual: " + actual);
            Assert.assertEquals(normalize(actual), normalize(expected), "Mismatch in: " + key);
        }

        System.out.println("All matched fields validated for: " + customer);
    }

    private String normalize(String value) {
        if (value == null || value.trim().equals("--")) return "0.00"; // Treat "--" as 0.00
        value = value.trim();

        // Try number normalization
        try {
            double d = Double.parseDouble(value);
            return String.format("%.2f", d); // Always 2 decimals
        } catch (NumberFormatException e) {
            // Not a number, try date normalization
        }

        // Try date normalization (dd/MM/yyyy or MM/dd/yyyy)
        try {
            List<String> dateFormats = Arrays.asList("dd/MM/yyyy", "MM/dd/yyyy");
            for (String format : dateFormats) {
                try {
                    java.time.LocalDate date = java.time.LocalDate.parse(
                        value, java.time.format.DateTimeFormatter.ofPattern(format)
                    );
                    return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception ignore) {}
            }
        } catch (Exception ignore) {}

        // Return as-is if neither number nor date
        return value;
    }

}
