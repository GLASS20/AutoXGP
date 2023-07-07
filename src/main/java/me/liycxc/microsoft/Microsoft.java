package me.liycxc.microsoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.liycxc.AppMain;
import me.liycxc.utils.CookieUtils;
import me.liycxc.utils.Generator;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static me.liycxc.AppMain.DRIVER_WAIT;
import static me.liycxc.AppMain.DRIVER_WAIT_ERROR;

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
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static ObjectNode loginAlipay(FirefoxDriver driver) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        driver.get("https://www.alipay.com");

        boolean loadCookies = CookieUtils.loadCookies(driver);

        driver.navigate().refresh();

        try {
            driver.get("https://auth.alipay.com/login/index.htm");

            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));
            WebDriverWait threeWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_ERROR));

            WebElement loginType = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[text()='账密登录']")));
            loginType.click();

            int index = 0;
            do {
                index++;
                if (index > 15) {
                    throw new Exception("Login account error");
                }
                try {
                    WebElement usernameText = threeWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='J-input-user']")));
                    usernameText.clear();
                    Actions actions = new Actions(driver);

                    for (char c : AppMain.API_ALIPAY_USERNAME.toCharArray()) {
                        int delay = new Random().nextInt(300) + 50;

                        // 添加延迟
                        Thread.sleep(delay);

                        // 输入字符
                        usernameText.sendKeys(String.valueOf(c));
                    }

                    Thread.sleep(500);

                    WebElement passwordText = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='password_rsainput']")));
                    passwordText.clear();

                    for (char c : AppMain.API_ALIPAY_PASSWORD.toCharArray()) {
                        int delay = new Random().nextInt(300) + 50;

                        // 添加延迟
                        Thread.sleep(delay);

                        // 输入字符
                        passwordText.sendKeys(String.valueOf(c));
                    }

                    Thread.sleep(500);

                    WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='J-login-btn']")));
                    actions.click(next).build().perform();

                    Thread.sleep(2000);
                } catch (Exception ignored) {
                }
            } while (!driver.getCurrentUrl().toLowerCase().startsWith("https://b.alipay.com"));

            System.out.println("11234" + driver.getCurrentUrl());
        } catch (Exception exception) {
            exception.printStackTrace();
            json.put("code", 1);
            json.put("msg", ExceptionUtils.getFullStackTrace(exception));
            return json;
        }

        json.put("code", loadCookies ? 0 : 1);
        json.put("msg", loadCookies ? "cookies ok" : "cookies error");

        return json;
    }

    public static ObjectNode loginAccount(FirefoxDriver driver, String[] account) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        try {
            driver.get("https://login.live.com");
            Actions actions = new Actions(driver);
            // WebWaiter
            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));

            // loginfmt
            WebElement loginfmt = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("loginfmt")));

            loginfmt.sendKeys(account[0]);

            // idSIButton9
            WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
            actions.click(next).perform();


            // passwd
            WebElement passwd = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("passwd")));
            passwd.sendKeys(account[1]);

            // idSIButton9
            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
            actions.click(next).perform();


            // Your Microsoft account brings everything together
            try {
                next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Continue']")));
                while (true) {
                    try {
                        next.click();
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception ignored) {
            }

            // idSIButton9
            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
            actions.click(next).perform();

            try {
                // idSIButton9
                next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
                actions.click(next).perform();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            System.out.println("Login " + account[0] + " " + account[1] + " succeed ");

            json.put("code", 0);
            json.put("msg", "ok");
            return json;
        } catch (Exception exception) {
            json.put("code", 1);
            json.put("msg", ExceptionUtils.getFullStackTrace(exception));
            return json;
        }
    }

    public static ObjectNode gamePassNew(FirefoxDriver driver, String[] account) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        try {
            driver.get("https://www.xbox.com/zh-HK/xbox-game-pass/pc-game-pass?xr=shellnav");
            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));
            WebDriverWait threeWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_ERROR));

            // click login by cookie
            int index = 0;
            try {
                WebElement login = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("mectrl_main_trigger")));
                while (true) {
                    if (index > 5) {
                        break;
                    }
                    try {
                        login.click();
                        index++;
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            while (!driver.getCurrentUrl().toLowerCase().contains("login.live.com")) {
                Thread.sleep(50);
            }

            driver.switchTo().defaultContent();
            driver.navigate().refresh();

            // Sign in
            // loginfmt
            Actions actions = new Actions(driver);
            WebElement loginfmt = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='loginfmt' and @type='email']")));
            actions.moveToElement(loginfmt).sendKeys(account[0]).perform();

            // Next
            // idSIButton9
            WebElement next = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='idSIButton9' and @value='Next']")));
            while (true) {
                try {
                    actions.moveToElement(next).moveByOffset(5, 5).click().perform();
                } catch (Exception exception) {
                    break;
                }
            }

            // Enter password
            WebElement password = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='passwd' and @type='password']")));
            password.sendKeys(account[1]);

            // idSIButton9
            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='idSIButton9' and @value='Sign in']")));
            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }
            // Stay signed in?
            // idSIButton9
            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='idSIButton9' and @value='Yes']")));
            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }

            // first set xbox archive
            driver.switchTo().defaultContent();

            try {
                while (true) {
                    // Xbox archive nameid
                    WebElement nameId = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='create-account-gamertag-suggestion-1']")));
                    while (true) {
                        try {
                            nameId.click();
                        } catch (Exception exception) {
                            break;
                        }
                    }

                    // inline-continue-control
                    WebElement letGo = driverWait.until(ExpectedConditions.elementToBeClickable(By.id("inline-continue-control")));
                    try {
                        letGo.click();
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception exception) {
                // exception.printStackTrace();
            }

            // click joinus xd
            index = 0;

            // c-call-to-action c-glyph xbstorebuy xbstoreDynAdd storeDynAdded
            while (!driver.getPageSource().contains("PC 版 1 個月")) {
                try {
                    WebElement joinUs = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='JavaScript:void(0);' and @role='button' and @class='c-call-to-action c-glyph xbstorebuy xbstoreDynAdd storeDynAdded']")));
                    joinUs.click();
                } catch (Exception exception) {
                    index++;
                    Thread.sleep(1000);
                }

                if (index > 10) {
                    index = 0;
                    driver.navigate().refresh();
                }
            }

            // 下一步
            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='下一步']")));
            while (!driver.getPageSource().contains("開始！新增付款方式。")) {
                try {
                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            Thread.sleep(50);

            driver.switchTo().defaultContent();
            driver.switchTo().frame("purchase-sdk-hosted-iframe");

            Thread.sleep(1500);

            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), \"下一步\")]")));
            while (!driver.getPageSource().contains("選擇付款方式")) {
                try {
                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            Thread.sleep(1000);

            WebElement alipay1 = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("displayId_ewallet")));
            while (!driver.getPageSource().contains("<h1 id=\"pidlddc-text-paymentMethodSubGroupPageHeading_ewallet\" class=\"pidlddc-static-text pidlddc-heading\" aria-label=\"eWallet\">eWallet</h1>")) {
                try {
                    alipay1.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            Thread.sleep(1000);

            WebElement alipay2 = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("displayId_ewallet_alipay_billing_agreement")));
            while (!driver.getPageSource().contains("新增您的支付寶帳戶")) {
                try {
                    alipay2.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            Thread.sleep(1000);

            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='下一步']")));
            while (!driver.getPageSource().contains("以您的支付寶電子錢包掃描 QR 代碼。")) {
                try {
                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            Thread.sleep(1000);

            while (true) {
                try {
                    WebElement alipay3 = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("pidlddc-hyperlink-alipayQrCodeChallengeRedirectionLink")));
                    alipay3.click();

                    driver.switchTo().defaultContent();
                    driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));

                    while (true) {
                        try {
                            WebElement payPasswordRsainput = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("payPassword_rsainput")));
                            payPasswordRsainput.sendKeys(AppMain.API_ALIPAY_PAYKEY);

                            WebElement jSubmit = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("J_submit")));
                            while (true) {
                                try {
                                    jSubmit.click();
                                } catch (Exception exception) {
                                    break;
                                }
                            }
                            break;
                        } catch (Exception exception) {
                            driver.navigate().refresh();
                        }
                    }

                    driver.switchTo().window(driver.getWindowHandles().stream().toList().get(0));
                    driver.switchTo().defaultContent();
                    driver.switchTo().frame("purchase-sdk-hosted-iframe");

                    break;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@aria-label='繼續']")));
            while (!driver.getPageSource().contains("我們需要您的設定檔地址")) {
                try {
                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            while (!driver.getPageSource().contains("付款方式")) {
                try {
                    // In Mihoyo xd
                    WebElement city = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("city")));
                    city.clear();
                    city.sendKeys("Shanghai");

                    WebElement address = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("address_line1")));
                    address.clear();
                    address.sendKeys("5th floor, Building C, No. 700 Yishan Road, Xuhui District, Shanghai");

                    next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value=\"儲存\"]")));

                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1500);
                }
            }

            Thread.sleep(1500);

            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), '訂閱')]")));
            while (!driver.getPageSource().contains("感謝您加入")) {
                try {
                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            driver.switchTo().defaultContent();

            json.put("code", 0);
            json.put("msg", "ok");
            return json;
        } catch (Exception exception) {
            exception.printStackTrace();
            json.put("code", 1);
            json.put("msg", ExceptionUtils.getFullStackTrace(exception));
            return json;
        }
    }

    @Deprecated(since = "Please use func gamePassNew")
    public static ObjectNode gamePassByCookie(FirefoxDriver driver, String[] account) {
        // https://www.xbox.com/en-US/xbox-game-pass
        ObjectNode json = OBJECT_MAPPER.createObjectNode();
        try {
            driver.get("https://www.xbox.com/zh-HK/xbox-game-pass/pc-game-pass?xr=shellnav");

            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));
            WebDriverWait threeWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_ERROR));

            // mectrl_headerPicture
            try {
                WebElement login = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("mectrl_main_trigger")));
                while (true) {
                    try {
                        login.click();
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            // first set xbox archive
            try {
                int index = 1;
                do {
                    // Xbox archive nameid
                    WebElement nameId = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("create-account-gamertag-suggestion-" + index)));
                    while (true) {
                        try {
                            nameId.click();
                        } catch (Exception exception) {
                            break;
                        }
                    }

                    try {
                        // id failure
                        if (index > 4) {
                            json.put("code", 1);
                            json.put("msg", "Set xbox id error");
                            return json;
                        }

                        WebElement failure = threeWait.until(ExpectedConditions.presenceOfElementLocated(By.className("failure")));
                        if (!failure.isDisplayed()) {
                            break;
                        }
                        index++;
                    } catch (Exception exception) {
                        break;
                    }
                } while (true);

                // inline-continue-control
                WebElement letGo = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("inline-continue-control")));
                while (true) {
                    try {
                        letGo.click();
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            // click joinus xd
            int index = 0;
            while (true) {
                if (index > 3) {
                    json.put("code", 1);
                    json.put("msg", "Check xbox pass game Internet error");
                    return json;
                }
                try {
                    // c-call-to-action c-glyph xbstorebuy xbstoreDynAdd storeDynAdded
                    WebElement joinUs = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='JavaScript:void(0);' and @role='button' and @class='c-call-to-action c-glyph xbstorebuy xbstoreDynAdd storeDynAdded']")));
                    while (true) {
                        try {
                            joinUs.click();
                        } catch (Exception exception) {
                            break;
                        }
                    }
                    break;
                } catch (Exception exception) {
                    index++;
                    driver.navigate().refresh();
                }
            }

            try {
                // loginfmt
                WebElement loginfmt = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("loginfmt")));

                loginfmt.sendKeys(account[0]);

                // idSIButton9
                WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
                while (true) {
                    try {
                        next.click();
                    } catch (Exception exception) {
                        break;
                    }
                }


                WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_ERROR));

                // usernameError
                try {
                    WebElement error = errorWait.until(ExpectedConditions.presenceOfElementLocated(By.id("usernameError")));
                    if (error.isDisplayed()) {
                        json.put("code", 1);
                        json.put("msg", "Username error");
                        return json;
                    }
                } catch (Exception ignored) {
                }

                // idSIButton9
                next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
                while (true) {
                    try {
                        next.click();
                    } catch (Exception exception) {
                        break;
                    }
                }

                // passwd
                WebElement passwd = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='passwd']")));
                passwd.clear();
                passwd.sendKeys(account[1]);

                // idSIButton9
                next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
                while (true) {
                    try {
                        next.click();
                    } catch (Exception exception) {
                        break;
                    }
                }

                // idSIButton9
                next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
                while (true) {
                    try {
                        next.click();
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception exception) {
                System.out.println("No 2 check");
            }

            // 下一步
            WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.Column-module__col6___keGm9.ContextualStoreProductDetailsPage-module__paddingLeft0___gaLHu.ContextualStoreProductDetailsPage-module__paddingRight0___gAqxV button.ContextualStoreProductDetailsPage-module__actionButton___wDRb8")));
            Actions action = new Actions(driver);
            action.click(next).perform();

            Thread.sleep(50);

            driver.switchTo().defaultContent();
            driver.switchTo().frame("purchase-sdk-hosted-iframe");

            // add pay way
            WebElement nextButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='primary--DXmYtnzQ base--kY64RzQE']")));

            // I don't know what this code is for, but don't delete it, it will cause the code to not run smoothly
            nextButton.click();

            // alipay
            WebElement alipay1 = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("displayId_ewallet")));
            while (true) {
                try {
                    alipay1.click();
                } catch (Exception exception) {
                    break;
                }
            }

            // alipay
            WebElement alipay2 = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("displayId_ewallet_alipay_billing_agreement")));
            while (true) {
                try {
                    alipay2.click();
                } catch (Exception exception) {
                    break;
                }
            }

            // next
            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("pidlddc-button-saveNextButton")));
            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }

            // login alipay
            WebElement alipay3 = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("pidlddc-hyperlink-alipayQrCodeChallengeRedirectionLink")));
            alipay3.click();

            Thread.sleep(1500);

            while (true) {
                try {
                    driver.switchTo().window(driver.getWindowHandles().stream().toList().get(1));
                    break;
                } catch (Exception e) {
                    alipay3.click();
                }
            }

            driver.navigate().refresh();

            System.out.println(driver.getCurrentUrl());

            driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));

            try {
                driver.switchTo().defaultContent();
                driver.navigate().refresh();
                WebElement payPasswordRsainput = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("payPassword_rsainput")));
                payPasswordRsainput.sendKeys(AppMain.API_ALIPAY_PAYKEY);

                WebElement jSubmit = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("J_submit")));
                while (true) {
                    try {
                        jSubmit.click();
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                json.put("code", 1);
                json.put("msg", "alipay error");
                return json;
            }

            driver.switchTo().window(driver.getWindowHandles().stream().toList().get(0));
            driver.switchTo().defaultContent();
            driver.switchTo().frame("purchase-sdk-hosted-iframe");

            driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));

            next = driverWait.until(ExpectedConditions.elementToBeClickable(By.id("pidlddc-button-alipayContinueButton")));
            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }

            String js = "document.getElementById('pidlddc-button-alipayContinueButton').click()";
            driver.executeScript(js);

            // In Mihoyo xd
            try {
                WebElement city = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("city")));
                city.sendKeys("Shanghai");

                WebElement address = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("address_line1")));
                address.sendKeys("5th floor, Building C, No. 700 Yishan Road, Xuhui District, Shanghai");

                next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("pidlddc-button-saveButton")));
                next.click();

                while (true) {
                    try {
                        nextButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='primary--DXmYtnzQ base--kY64RzQE']")));
                        break;
                    } catch (Exception exception) {
                        next.click();
                    }
                }

                nextButton.click();
            } catch (Exception exception) {
                exception.printStackTrace();
            }


            try {
                next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='ContextualStoreProductDetailsPage-module__width100___Rn+5k ContextualStoreProductDetailsPage-module__marginTop1___fzkzL ContextualStoreProductDetailsPage-module__actionButton___wDRb8 ContextualStoreProductDetailsPage-module__uiExpActionButton___5lfV2 Button-module__defaultBase___c7wIT Button-module__heroMediumBorderRadius___dUTPQ Button-module__buttonBase___olICK Button-module__textNoUnderline___kHdUB Button-module__typeBrand___MMuct Button-module__sizeMedium___T+8s+ Button-module__overlayModeSolid___v6EcO']")));
                while (true) {
                    try {
                        next.click();
                    } catch (Exception exception) {
                        break;
                    }
                }

                nextButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='primary--DXmYtnzQ base--kY64RzQE']")));

                // I don't know what this code is for, but don't delete it, it will cause the code to not run smoothly
                while (true) {
                    try {
                        nextButton.click();
                    } catch (Exception exception) {
                        break;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                driver.switchTo().defaultContent();
                WebElement hello = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@class='ThankYouPage-module__thanksForJoining___igWv1']")));
                if (hello.isDisplayed()) {
                    json.put("code", 0);
                    json.put("msg", "Ok");
                    return json;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            json.put("code", 1);
            json.put("msg", "Thank you page not display");
            return json;
        } catch (Exception exception) {
            exception.printStackTrace();
            json.put("code", 1);
            json.put("msg", ExceptionUtils.getFullStackTrace(exception));
            return json;
        }
    }

    public static ObjectNode archiveByCookie(FirefoxDriver driver, String playerId) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));
        String player = playerId;

        try {
            driver.get("https://www.minecraft.net/en-us/login");

            WebElement loginButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='btn btn-primary btn-block signin-msa-button flex-wrap']")));
            while (!driver.getPageSource().contains("Ready to play? Download the Minecraft Launcher to access all your Minecraft games for Windows, Mac, or Linux!")) {
                try {
                    loginButton.click();
                } catch (Exception exception) {
                    Thread.sleep(2000);
                }
            }

            driver.get("https://www.minecraft.net/en-us/msaprofile/redeem?setupProfile=true");

            Thread.sleep(2000);

            WebElement inputText = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='change-profile-name__profile-textbox']")));
            WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn btn__success redeem__text-transform']")));

            while (!driver.getPageSource().contains("You’re all set up - It’s time to dive in! ")) {
                try {
                    inputText.clear();
                    inputText.sendKeys(player);
                    Thread.sleep(50);
                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }

                if (driver.getPageSource().contains("This profile name already exists")) {
                    player = Generator.generatePlayerId();
                }
            }
        } catch (Exception exception) {
            json.put("code", 1);
            json.put("msg", exception.getMessage());
            json.put("player", player);
            return json;
        }

        json.put("code", 0);
        json.put("msg", "ok");
        json.put("player", player);
        return json;
    }

    public static ObjectNode backMoney(FirefoxDriver driver, String[] account) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT));

        try {
            driver.get("https://account.microsoft.com/services/pcgamepass/cancel?fref=billing-cancel");

            WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Continue']")));

            while (!driver.getPageSource().contains("Find something that ")) {
                try {
                    next.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            WebElement cbb = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='Cancel subscription']")));
            while (!driver.getPageSource().contains("Select your cancellation date")) {
                try {
                    cbb.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }

            WebElement kcb = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Cancel immediately and get a refund']")));
            List<WebElement> scs = driverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//button[@aria-label='Cancel subscription']")));
            while (!driver.getPageSource().contains("Goodbye for now!")) {
                try {
                    kcb.click();
                } catch (Exception ignored) {
                }
                Thread.sleep(500);
                try {
                    scs.get(0).click();
                } catch (Exception ignored) {
                }
                Thread.sleep(500);
                try {
                    scs.get(1).click();
                } catch (Exception ignored) {
                }
                Thread.sleep(1000);
            }

            driver.get("https://account.microsoft.com/billing/payments?fref=home.drawers.payment-options.manage-payment");
            Thread.sleep(3000);
            if (driver.getPageSource().contains("Enter password")) {
                WebElement passwd = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='passwd']")));
                while (!driver.getPageSource().contains("Manage your payments")) {
                    try {
                        passwd.clear();
                        passwd.sendKeys(account[1]);
                        passwd.sendKeys(Keys.ENTER);
                    } catch (Exception exception) {
                        Thread.sleep(1000);
                    }
                }
            }

            WebElement dele = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='Remove Alipay']")));
            while (!driver.getPageSource().contains("Remove Alipay account")) {
                try {
                    dele.click();
                } catch (Exception exception) {
                    Thread.sleep(500);
                }
            }

            WebElement deled = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Remove']")));
            while (!driver.getPageSource().contains("Alipay account has been removed from your account!")) {
                try {
                    deled.click();
                } catch (Exception exception) {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception exception) {
            json.put("code", 1);
            json.put("msg", exception.getMessage());
            return json;
        }

        json.put("code", 0);
        json.put("msg", "ok");
        return json;
    }
}