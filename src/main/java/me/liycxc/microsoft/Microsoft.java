package me.liycxc.microsoft;

import lombok.experimental.UtilityClass;
import me.liycxc.utils.EmailGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static me.liycxc.runner.Driver.driver;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-06-30
 * @time: 22:26
 */
@UtilityClass
public class Microsoft {
    public static String[] createAccount() {
        String email = "lyglass20";
        String password = EmailGenerator.generateRandomPassword();

        driver.get("https://login.live.com");
        // Driver waiter
        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // CreatOne element
        WebElement creatOne = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signup")));
        creatOne.click();

        // Get new mail element
        WebElement getNewMail = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("liveSwitch")));
        getNewMail.click();

        do {
            // Email name
            WebElement mailText = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MemberName")));
            mailText.clear();
            mailText.sendKeys(email);

            WebElement iSignupAction = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iSignupAction")));
            iSignupAction.click();

            try {
                WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                WebElement memberNameError = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MemberNameError")));
                if (memberNameError.isDisplayed()) {
                    email = EmailGenerator.generateRandomEmail();
                    continue;
                }
            }catch (Exception ignored) {
            }

            WebElement passwordInput = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("PasswordInput")));
            passwordInput.sendKeys(password);

            iSignupAction = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iSignupAction")));
            iSignupAction.click();


        } while (true);

        // return new String[]{" ", " "};
    }
}
