package com.cst438;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeleteStudentTest {

    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/amaur/Desktop/chromedriver-win64/chromedriver.exe";
    public static final String URL = "http://localhost:3000";
    public static final String DELETE_STUDENT_NAME = "Delete Me";
    public static final int SLEEP_DURATION = 1000; // 1 second.

    @Test
    public void deleteStudentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(URL);
            Thread.sleep(SLEEP_DURATION);

            // Locate and click the student you want to delete
            WebElement studentElement = driver.findElement(By.xpath("//tr[td='" + DELETE_STUDENT_NAME + "']"));
            assertNotNull(studentElement, "Student to delete not found in the student list.");

            // Click the Delete button
            driver.findElement(By.id("deleteStudent")).click();
            Thread.sleep(SLEEP_DURATION);

            // Confirm the deletion
            driver.findElement(By.id("confirmDelete")).click();
            Thread.sleep(SLEEP_DURATION);

            // Verify that the student is deleted
            assertNull(driver.findElements(By.xpath("//tr[td='" + DELETE_STUDENT_NAME + "']")));

        } catch (Exception ex) {
            throw ex;
        } finally {
            driver.quit();
        }
    }

	private void assertNotNull(WebElement studentElement, String string) {
		// TODO Auto-generated method stub
		
	}
}
