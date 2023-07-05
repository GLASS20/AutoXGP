package me.liycxc.runner;

import me.liycxc.AppMain;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
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
    public static FirefoxDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

        FirefoxDriver driver;
        FirefoxOptions options = new FirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile(new File(AppMain.DRIVER_DATA));
        options.setProfile(firefoxProfile);
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", "en-US"); // 设置为英语（美国）语言片
        options.setCapability("prefs", prefs);

        driver = new FirefoxDriver(options);

        return driver;
    }
}
