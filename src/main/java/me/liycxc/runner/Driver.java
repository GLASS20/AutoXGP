package me.liycxc.runner;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-06-30
 * @time: 22:23
 */
public class Driver {
    public static ChromeDriver driver;
    public static void init() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", "en-US"); // 设置为英语（美国）语言
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
    }
}
