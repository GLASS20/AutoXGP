package me.liycxc.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.liycxc.microsoft.Microsoft;
import me.liycxc.utils.CookieUtils;
import org.openqa.selenium.WebDriver;
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

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("code", 0);
        json.put("info", "Save cookies successful");

        return json.toString();
    }

    @GetMapping("/xgp")
    public static String createMicrosoft() throws InterruptedException {
        WebDriver driver = Driver.getDriver();
        driver.manage().deleteAllCookies();

        String[] account = new String[]{"metelngonyar@hotmail.com", "Gn37ms56"}; // Mail.getMailByApi();

        System.out.println("Login " + account[0] + " " + account[1]);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode().objectNode();

        ObjectNode loginJson = Microsoft.loginAccount(driver, account);
        if (loginJson.get("code").asInt() != 0) {
            json.put("code", loginJson.get("code").asInt());
            json.put("step", "Login microsoft account");
            json.put("error", loginJson.get("msg").toString());
            return json.toString();
        }

        ObjectNode xgpJson = Microsoft.gamePassByCookie(driver, account);
        if (xgpJson.get("code").asInt() != 0) {
            json.put("code", xgpJson.get("code").asInt());
            json.put("step", "Subscribe to xbox game pass");
            json.put("error", xgpJson.get("msg").toString());
            return json.toString();
        }

        json.put("code", 0);
        json.put("account", account[0] + ":" + account[1]);
        return json.toString();
    }
}
