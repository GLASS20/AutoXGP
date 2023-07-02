package me.liycxc.microsoft;

import lombok.experimental.UtilityClass;
import me.liycxc.utils.EmailGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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
public class Microsoft {
    public static HashMap<Boolean,String> loginAccount(String[] account) {
        driver.get("https://login.live.com");
        // WebWaiter
        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // loginfmt
        WebElement loginfmt = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("loginfmt")));

        loginfmt.sendKeys(account[0]);

        // idSIButton9
        WebElement next = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSIButton9")));
        next.click();

        WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofMillis(1500));

        // usernameError
        try {
            WebElement error = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usernameError")));
            if (error.isDisplayed()) {
                return errorMap("Username Error");
            }
        } catch (Exception ignored) {}

        // passwd
        WebElement passwd = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("passwd")));
        passwd.sendKeys(account[1]);

        // idSIButton9
        next = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSIButton9")));
        next.click();

        try {
            WebElement error = errorWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("passwordError")));
            if (error.isDisplayed()) {
                return errorMap("Password Error");
            }
        } catch (Exception ignored) {}

        try {
            WebElement dontShow = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("KmsiCheckboxField")));
            if (!dontShow.isDisplayed()) {
                return errorMap("2FA");
            }
        } catch (Exception ignored) {}

        // idSIButton9
        next = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSIButton9")));
        next.click();

        return valueMap(true,null);
    }

    private static HashMap<Boolean,String> errorMap(String error) {
        return valueMap(false,error);
    }
    private static HashMap<Boolean,String> valueMap(boolean value, String error) {
        HashMap<Boolean,String> map = new HashMap<>();
        map.put(value,error);
        return map;
    }
}
