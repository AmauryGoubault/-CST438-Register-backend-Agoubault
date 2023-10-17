package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit; // Import TimeUnit

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddStudentTest {

    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/amaur/Desktop/chromedriver-win64/chromedriver.exe";
    public static final String URL = "http://localhost:3000";
    public static final String TEST_STUDENT_NAME = "John Doe";
    public static final int SLEEP_DURATION = 1000; // 1 second.

    @Test
    public void addStudentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click "Add Student" button
            driver.findElement(By.id("addStudent")).click();
            Thread.sleep(SLEEP_DURATION);

            // Enter student name and click Add button
            driver.findElement(By.id("studentName")).sendKeys(TEST_STUDENT_NAME);
            driver.findElement(By.id("add")).click();
            Thread.sleep(SLEEP_DURATION);

            // Verify that the new student is added
            WebElement studentElement = driver.findElement(By.xpath("//tr[td='" + TEST_STUDENT_NAME + "']"));
            assertNotNull(studentElement, "Test student not found in the student list.");

        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
}
