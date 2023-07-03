package me.liycxc.microsoft;

import me.liycxc.AppMain;
import me.shivzee.JMailTM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
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
        } catch (Exception ignored) {
        }

        // idSIButton9
        next = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("idSIButton9")));
        next.click();

        // iPageTitle
        AtomicBoolean helpLock = new AtomicBoolean(false);
        try {
            WebDriverWait helpWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            List<WebElement> iPageTitle = helpWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("iPageTitle")));
            iPageTitle.forEach(element -> {
                if ("Help us protect your account".equals(element.getText())) {
                    helpLock.set(true);
                }
            });
        } catch (Exception ignored) {
        }

        String email = null;
        if (helpLock.get()) {
            System.out.println("Account is locked");
            try {
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

                email = mailTm.getSelf().getEmail();
                System.out.println(mailTm.getSelf().getEmail() + " " + account[1]);
            } catch (Exception exception) {
                return errorMap("Set SecurityInfo error at " + exception);
            }
        }

        System.out.println("Login " + account[0] + " " + account[1] + " succeed " + email);
        return valueMap(true, email);
    }

    public static HashMap<Boolean, String> gamePassByCookie(ChromeDriver driver, String[] account) {
        // https://www.xbox.com/en-US/xbox-game-pass
        // 新的标签页
        String retValue = null;

        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://www.xbox.com/zh-HK/xbox-game-pass/pc-game-pass?xr=shellnav");

        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait threeWait = new WebDriverWait(driver, Duration.ofSeconds(3));

        // mectrl_headerPicture
        WebElement login = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mectrl_main_trigger")));
        login.click();

        int index = 1;
        do {
            // Xbox archive nameid
            WebElement nameId = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-account-gamertag-suggestion-" + index)));
            nameId.click();

            try {
                // id failure
                if (index > 4) {
                    return errorMap("What's up?");
                }

                WebElement failure = threeWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("failure")));
                if (!failure.isDisplayed()) {
                    break;
                }
                index++;
            } catch (Exception exception) {
                break;
            }
        } while (true);

        // inline-continue-control
        WebElement letGo = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inline-continue-control")));
        letGo.click();


        // c-call-to-action c-glyph xbstorebuy xbstoreDynAdd storeDynAdded
        WebElement joinUs = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("立即加入")));
        joinUs.click();

        // 下一步
        WebElement next = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.Column-module__col6___keGm9.ContextualStoreProductDetailsPage-module__paddingLeft0___gaLHu.ContextualStoreProductDetailsPage-module__paddingRight0___gAqxV button.ContextualStoreProductDetailsPage-module__actionButton___wDRb8")));
        next.click();

        // add pay way
        WebElement newPayWay = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.optionContainer--A9GXUhvU.lightweight--IYKcwqan.base--kY64RzQE")));
        newPayWay.click();

        // alipay
        WebElement alipay1 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pidlddc-input-property pidlddc-input-buttonlist pidlddc-input-undefined pidlddc-input-paymentMethod")));
        alipay1.click();

        // alipay
        WebElement alipay2 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("displayId_ewallet_alipay_billing_agreement")));
        alipay2.click();

        // next
        next = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pidlddc-button-saveNextButton")));
        next.click();

        // login alipay
        WebElement alipay3 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pidlddc-hyperlink-alipayQrCodeChallengeRedirectionLink")));
        alipay3.click();

        // alipay qrlogin
        WebElement alipay4 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("J-qrcode-target")));
        alipay4.click();

        // alipay username
        WebElement alipay5 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("J-input-user")));
        alipay5.sendKeys(AppMain.API_ALIPAY_ACCOUNT);

        // alipay password
        WebElement alipay6 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password_rsainput")));
        alipay6.sendKeys(AppMain.API_ALIPAY_PASSWORD);

        // alipay login
        WebElement alipay7 = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("J-login-btn")));
        alipay7.click();


        return valueMap(true, retValue);
    }

    private static HashMap<Boolean, String> errorMap(String error) {
        return valueMap(false, error);
    }

    private static HashMap<Boolean, String> valueMap(boolean value, String str) {
        HashMap<Boolean, String> map = new HashMap<>();
        map.put(value, str);
        return map;
    }
}
