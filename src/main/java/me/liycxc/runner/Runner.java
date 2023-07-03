package me.liycxc.runner;

import me.liycxc.microsoft.Microsoft;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    @GetMapping("/alipay")
    public static String alipay() throws JSONException, IOException, InterruptedException {
        ChromeDriver driver = Driver.getDriver();
        driver.manage().deleteAllCookies();

        driver.get("https://auth.alipay.com/login/index.htm?goto=https%3A%2F%2Fwww.alipay.com%2F");

        while (!"https://www.alipay.com/".equals(driver.getCurrentUrl())) {
            Thread.sleep(1000);
        }

        // 获取当前的cookie
        Set<Cookie> cookies = driver.manage().getCookies();

        // 转换为JSON格式
        JSONArray cookieArray = new JSONArray();
        for (Cookie cookie : cookies) {
            JSONObject cookieObject = new JSONObject();
            cookieObject.put("name", cookie.getName());
            cookieObject.put("value", cookie.getValue());
            cookieObject.put("domain", cookie.getDomain());
            cookieObject.put("path", cookie.getPath());
            if (cookie.getExpiry() != null) {
                cookieObject.put("expiry", cookie.getExpiry().getTime());
            }
            cookieObject.put("secure", cookie.isSecure());
            cookieObject.put("httpOnly", cookie.isHttpOnly());
            cookieArray.put(cookieObject);
        }

        // 保存到JSON文件
        FileWriter fileWriter = new FileWriter("cookies.json");
        fileWriter.write(cookieArray.toString());
        fileWriter.close();

        return "Save cookies succeed";
    }

    @GetMapping("/xgp")
    public static String createMicrosoft() throws IOException, JSONException {
        ChromeDriver driver = Driver.getDriver();
        driver.manage().deleteAllCookies();

        // 从JSON文件加载cookie
        FileReader fileReader = new FileReader("cookies.json");
        StringBuilder jsonContent = new StringBuilder();
        int character;
        while ((character = fileReader.read()) != -1) {
            jsonContent.append((char) character);
        }
        fileReader.close();

        // 转换为Cookie对象并加载到WebDriver中
        JSONArray cookieArray = new JSONArray(jsonContent.toString());
        for (int i = 0; i < cookieArray.length(); i++) {
            JSONObject cookieObject = cookieArray.getJSONObject(i);
            Cookie cookie = new Cookie(
                    cookieObject.getString("name"),
                    cookieObject.getString("value"),
                    cookieObject.getString("domain"),
                    cookieObject.getString("path"),
                    new Date(cookieObject.getLong("expiry")),
                    cookieObject.getBoolean("secure"),
                    cookieObject.getBoolean("httpOnly")
            );
            driver.manage().addCookie(cookie);
        }

        String[] account = new String[]{"metelngonyar@hotmail.com", "Gn37ms56"}; // Mail.getMailByApi();
//        if (account == null) {
//            return "[Error] " + "Login" + " api account is null";
//        }

        System.out.println("Login " + account[0] + " " + account[1]);

        String sm = null;
        HashMap<Boolean, String> loginValue = Microsoft.loginAccount(driver, account);
        for (Map.Entry<Boolean, String> i : loginValue.entrySet()) {
            if (!i.getKey()) {
                System.out.println("Login " + account[0] + " " + account[1] + " error: " + i.getValue());
                return ("[Error] " + " Login " + account[0] + ":" + account[1] + " " + i.getValue());
            } else {
                if (i.getValue() != null) {
                    sm = "  [SM] " + i.getValue() + ":" + account[1];
                }
            }
        }

        HashMap<Boolean, String> xgpValue = Microsoft.gamePassByCookie(driver, account);

        return "[Successful] " + account[0] + ":" + account[1] + (sm == null ? "" : sm);
    }
}
