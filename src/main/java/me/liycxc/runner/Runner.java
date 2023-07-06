package me.liycxc.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.liycxc.microsoft.Microsoft;
import me.liycxc.utils.CookieUtils;
import me.liycxc.utils.Generator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-07-01
 * @time: 22:18
 */
@RestController
public class Runner {

    @GetMapping("/load")
    public static String testSaveCookies() throws InterruptedException {
        WebDriver driver = Driver.getDriver();
        driver.manage().deleteAllCookies();

        driver.get("https://www.alipay.com");

        CookieUtils.loadCookies(driver);

        driver.navigate().refresh();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("code", 0);
        json.put("info", "Load cookies successful");

        return json.toString();
    }

    @GetMapping("/save")
    public static String testLoadCookies() throws InterruptedException {
        WebDriver driver = Driver.getDriver();
        driver.manage().deleteAllCookies();

        driver.get("https://auth.alipay.com/login/index.htm?goto=https%3A%2F%2Fwww.alipay.com%2F");

        // Qrlogin auto jump
        while (!"https://www.alipay.com/".equals(driver.getCurrentUrl())) {
            Thread.sleep(1000);
        }

        CookieUtils.saveCookies(driver);

        driver.quit();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("code", 0);
        json.put("info", "Save cookies successful");

        return json.toString();
    }

    @GetMapping("/sb")
    public static void sb() {
        FirefoxDriver driver = Driver.getDriver();
        // driver.manage().deleteAllCookies();

        Microsoft.loginAlipay(driver);
    }

    @GetMapping("/get")
    public static String createMicrosoft() throws InterruptedException {
        FirefoxDriver driver = Driver.getDriver();
        driver.manage().deleteAllCookies();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode().objectNode();

        // String[] account = new String[]{"baxaliwettigk@hotmail.com", "hYiaui13"};
        String[] account = new String[]{"reastkasisi0@hotmail.com", "USRO6v52"};
        //String[] account = Mail.getMailByApi();
        if (account == null || account[0] == null || account[1] == null) {
            json.put("code", -1);
            json.put("step", "Check account");
            driver.quit();
            return json.toString();
        } else {
            json.put("email", account[0]);
            json.put("password", account[1]);
        }

        System.out.println("Login " + account[0] + " " + account[1]);

        ObjectNode alipayJson = Microsoft.loginAlipay(driver);
        if (alipayJson.get("code").asInt() == 1) {
            json.put("code", 1);
            json.put("step", "Login alipay with cookie");
            json.put("error", alipayJson.get("msg").asText());
            // driver.quit();
            return json.toString();
        }

        ObjectNode loginJson = Microsoft.loginAccount(driver, account);

        if (loginJson.get("code").asInt() != 0) {
            json.put("code", loginJson.get("code").asInt());
            json.put("step", "Login microsoft account");
            json.put("error", loginJson.get("msg").asText());
            // driver.quit();
            return json.toString();
        }

        ObjectNode xgpJson = Microsoft.gamePassNew(driver, account);
        if (xgpJson.get("code").asInt() != 0) {
            json.put("code", xgpJson.get("code").asInt());
            json.put("step", "Subscribe to xbox game pass");
            json.put("error", xgpJson.get("msg").asText());
            //driver.quit();
            return json.toString();
        }

        ObjectNode archiveJson = Microsoft.archiveByCookie(driver, Generator.generatePlayerId());
        if (archiveJson.get("code").asInt() != 0) {
            json.put("code", archiveJson.get("code").asInt());
            json.put("step", "Set minecraft id");
            json.put("error", archiveJson.get("msg").asText());
            //driver.quit();
            return json.toString();
        }

        json.put("player", archiveJson.get("player").asText());

        ObjectNode backMoneyJson = Microsoft.backMoney(driver, account);
        if (backMoneyJson.get("code").asInt() != 0) {
            System.out.println("Back money error: " + account[0] + ":" + account[1]);
            json.put("code", backMoneyJson.get("code").asInt());
            json.put("step", "Back my money");
            json.put("error", backMoneyJson.get("msg").asText());
            //driver.quit();
            return json.toString();
        }

        json.put("code", 0);
        //driver.quit();
        return json.toString();
    }
}
