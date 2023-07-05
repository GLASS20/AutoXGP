package me.liycxc.microsoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.liycxc.AppMain;
import me.liycxc.utils.CookieUtils;
import me.shivzee.JMailTM;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static ObjectNode loginAlipay(FirefoxDriver driver) throws InterruptedException {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        driver.get("https://www.alipay.com");

        boolean loadCookies = CookieUtils.loadCookies(driver);

        driver.navigate().refresh();

        Thread.sleep(1500);

        json.put("code", loadCookies ? 0 : 1);
        json.put("msg", loadCookies ? "cookies ok" : "cookies error");

        return json;
    }

    public static ObjectNode loginAccount(FirefoxDriver driver, String[] account) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        try {
            driver.get("https://login.live.com");

            // WebWaiter
            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(5));

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


            WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofMillis(1500));

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

            // passwd
            WebElement passwd = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("passwd")));
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

            try {
                WebElement error = errorWait.until(ExpectedConditions.presenceOfElementLocated(By.id("passwordError")));
                if (error.isDisplayed()) {
                    json.put("code", 1);
                    json.put("msg", "Password error");
                    return json;
                }
            } catch (Exception ignored) {
            }

            try {
                WebElement dontShow = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("KmsiCheckboxField")));
                if (!dontShow.isDisplayed()) {
                    json.put("code", 1);
                    json.put("msg", "2FA");
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
                        json.put("code", 1);
                        json.put("msg", "Email error at " + mail);
                        return json;
                    }

                    JMailTM mailTm = (JMailTM) mail;

                    // EmailAddress
                    WebElement emailAddress = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("EmailAddress")));
                    emailAddress.sendKeys(mailTm.getSelf().getEmail());

                    // iNext
                    next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("iNext")));
                    while (true) {
                        try {
                            next.click();
                        } catch (Exception exception) {
                            break;
                        }
                    }

                    String securityCode = Mail.getCodeByMail(mailTm);
                    if (securityCode.startsWith("error ")) {
                        json.put("code", 1);
                        json.put("msg", "Get SecurityCode at" + securityCode);
                        return json;
                    }

                    // iOttText
                    WebElement iOttText = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("iOttText")));
                    iOttText.sendKeys(securityCode);
                    iOttText.sendKeys("\n");

                    email = mailTm.getSelf().getEmail();
                    System.out.println(mailTm.getSelf().getEmail() + " " + account[1]);
                } catch (Exception exception) {
                    json.put("code", 1);
                    json.put("msg", "Set SecurityInfo error at " + exception);
                    return json;
                }
            }

            System.out.println("Login " + account[0] + " " + account[1] + " succeed " + email);

            json.put("code", 0);
            json.put("msg", "ok");
            json.put("SecureMailbox", email);
            return json;
        } catch (Exception exception) {
            json.put("code", 1);
            json.put("msg", exception.toString());
            return json;
        }
    }

    public static ObjectNode gamePassByCookie(FirefoxDriver driver, String[] account) throws InterruptedException {
        // https://www.xbox.com/en-US/xbox-game-pass
        ObjectNode json = OBJECT_MAPPER.createObjectNode();
        try {
            driver.get("https://www.xbox.com/zh-HK/xbox-game-pass/pc-game-pass?xr=shellnav");

            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebDriverWait threeWait = new WebDriverWait(driver, Duration.ofSeconds(10));

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

            index = 0;
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
                WebElement passwordDesc = threeWait.until(ExpectedConditions.presenceOfElementLocated(By.id("passwordDesc")));
                if (passwordDesc.isDisplayed()) {
                    passwdSender(account, driverWait);
                }
            } catch (Exception exception) {
                System.out.println("No 2 check");
            }

            // 下一步
            WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.Column-module__col6___keGm9.ContextualStoreProductDetailsPage-module__paddingLeft0___gaLHu.ContextualStoreProductDetailsPage-module__paddingRight0___gAqxV button.ContextualStoreProductDetailsPage-module__actionButton___wDRb8")));
            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }

            driver.switchTo().frame("purchase-sdk-hosted-iframe");

            // add pay way
            WebElement nextButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='primary--DXmYtnzQ base--kY64RzQE']")));

            // I don't know what this code is for, but don't delete it, it will cause the code to not run smoothly
            while (true) {
                try {
                    nextButton.click();
                } catch (Exception exception) {
                    break;
                }
            }

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

            driverWait = new WebDriverWait(driver, Duration.ofSeconds(20));

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
            } catch (Exception exception) {
                exception.printStackTrace();
                json.put("code", 1);
                json.put("msg", "alipay error");
                return json;
            }

            driver.switchTo().window(driver.getWindowHandles().stream().toList().get(0));
            driver.switchTo().frame("purchase-sdk-hosted-iframe");

            driverWait = new WebDriverWait(driver, Duration.ofSeconds(20));

            next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("pidlddc-button-alipayContinueButton")));

            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }

            // In Mihoyo xd
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
            json.put("msg", exception.toString());
            return json;
        }
    }

    public static ObjectNode archiveByCookie(FirefoxDriver driver, String playerId) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();
        try {
            // login to account
            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
            driver.get("https://www.minecraft.net/en-us/login");
            WebElement loginButton = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='btn btn-primary btn-block signin-msa-button flex-wrap']")));
            while (true) {
                try {
                    loginButton.click();
                } catch (Exception exception) {
                    break;
                }
            }

            while (!"https://www.minecraft.net/en-us/msaprofile".equals(driver.getCurrentUrl())) {
                Thread.sleep(1000);
            }

            // set minecraft archive
            driver.get("https://www.minecraft.net/en-us/msaprofile/redeem?setupProfile=true");
            WebElement inputText = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='change-profile-name__profile-textbox']")));
            inputText.sendKeys(playerId);

            WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn btn__success redeem__text-transform']")));
            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }

            json.put("code", 0);
            json.put("msg", "ok");
            return json;
        } catch (Exception exception) {
            exception.printStackTrace();
            json.put("code", 1);
            json.put("msg", exception.toString());
            return json;
        }
    }

    public static ObjectNode backMoney(FirefoxDriver driver, String[] account) {
        ObjectNode json = OBJECT_MAPPER.createObjectNode();

        try {
            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(20));

            driver.get("https://account.microsoft.com/services/pcgamepass/cancel?fref=billing-cancel");
            WebElement cbb = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("benefit-cancel")));
            while (true) {
                try {
                    cbb.click();
                } catch (Exception exception) {
                    break;
                }
            }

            WebElement kcb = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@aria-label='立即取消并获取退款']")));
            kcb.click();

            WebElement scs = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("cancel-select-cancel")));
            while (true) {
                try {
                    scs.click();
                } catch (Exception exception) {
                    break;
                }
            }

            while (true) {
                try {
                    WebElement goodby = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@alt='goodBye']")));
                    System.out.println(goodby.isDisplayed());
                    break;
                } catch (Exception ignored) {
                }
            }

            driver.get("https://account.microsoft.com/billing/payments?fref=home.drawers.payment-options.manage-payment");

            passwdSender(account, new WebDriverWait(driver, Duration.ofSeconds(5)));

            WebElement dele = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@aria-label='删除支付宝']")));
            dele.click();

            WebElement deled = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='删除']")));
            deled.click();

            try {
                WebElement info = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='已从你的帐户中删除支付宝帐户!']")));
                System.out.println(info);

                json.put("code", 0);
                json.put("msg", "ok");
                return json;
            } catch (Exception exception) {
                throw new Exception("dele pay way error");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            json.put("code", 1);
            json.put("msg", exception.toString());
            return json;
        }
    }

    private static void passwdSender(String[] account, WebDriverWait driverWait) {
        try {
            WebElement input = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("passwd")));
            input.sendKeys(account[1]);

            WebElement next = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9")));
            while (true) {
                try {
                    next.click();
                } catch (Exception exception) {
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }
}