package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit; // Import TimeUnit

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UpdateStudentTest {

    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/amaur/Desktop/chromedriver-win64/chromedriver.exe";
    public static final String URL = "http://localhost:3000";
    public static final String TEST_STUDENT_NAME = "John Doe";
    public static final String UPDATED_STUDENT_NAME = "Jane Smith";
    public static final int SLEEP_DURATION = 1000; // 1 second.

    @Test
    public void updateStudentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click the student you want to update
            WebElement studentElement = driver.findElement(By.xpath("//tr[td='" + TEST_STUDENT_NAME + "']"));
            assertNotNull(studentElement, "Test student not found in the student list.");

            // Click on the student to edit
            WebElement editButton = studentElement.findElement(By.id("editStudent"));
            editButton.click();
            Thread.sleep(SLEEP_DURATION);

            // Update the student's name
            WebElement nameInput = driver.findElement(By.id("studentName"));
            nameInput.clear();
            nameInput.sendKeys(UPDATED_STUDENT_NAME);

            // Click the Update button
            driver.findElement(By.id("update")).click();
            Thread.sleep(SLEEP_DURATION);

            // Verify that the student's name is updated
            WebElement updatedStudentElement = driver.findElement(By.xpath("//tr[td='" + UPDATED_STUDENT_NAME + "']"));
            assertNotNull(updatedStudentElement, "Updated student not found in the student list.");
            assertNull(driver.findElements(By.xpath("//tr[td='" + TEST_STUDENT_NAME + "']")));

        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }
}
