package me.liycxc.microsoft;

import me.shivzee.JMailTM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public static HashMap<Boolean,String> loginAccount(ChromeDriver driver,String[] account) {
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

        /// iPageTitle
        WebDriverWait helpWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        List<WebElement> iPageTitle = helpWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("iPageTitle")));
        AtomicBoolean helpLock = new AtomicBoolean(false);
        iPageTitle.forEach(element -> {
            if("Help us protect your account".equals(element.getText())) {
                helpLock.set(true);
            }
        });

        if (helpLock.get()) {
            System.out.println("Account is locked");
            Object mail = Mail.creatMail(account[1]);
            if (mail instanceof String) {
                return errorMap("Email error at " + mail);
            }
            JMailTM mailTm = (JMailTM) mail;

            // EmailAddress
            WebElement emailAddress = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("EmailAddress")));
            emailAddress.sendKeys(mailTm.getSelf().getEmail());

            // iNext
            next = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iNext")));
            next.click();

            String securityCode = Mail.getCodeByMail(mailTm);
            if (securityCode.startsWith("error ")) {
                return errorMap("Get SecurityCode at" + securityCode);
            }

            // iOttText
            WebElement iOttText = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("iOttText")));
            iOttText.sendKeys(securityCode);
            iOttText.sendKeys("\n");

            System.out.println(mailTm.getSelf().getEmail() + " " + account[1]);
        }

        System.out.println("Login " + account[0] + " " + account[1] + " succeed");
        return valueMap(true,null);
    }

    private static HashMap<Boolean,String> errorMap(String error) {
        return valueMap(false,error);
    }
    private static HashMap<Boolean,String> valueMap(boolean value, String str) {
        HashMap<Boolean,String> map = new HashMap<>();
        map.put(value,str);
        return map;
    }
}
