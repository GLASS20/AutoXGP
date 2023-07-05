package me.liycxc.utils;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Set;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-07-04
 * @time: 18:23
 */
public class CookieUtils {
    private static final String CONFIG_FILE_PATH = "cookies.txt";

    public static void saveCookies(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        try {
            if (new File(CONFIG_FILE_PATH).exists()) {
                new File(CONFIG_FILE_PATH).delete();
            }
            new File(CONFIG_FILE_PATH).createNewFile();
            FileOutputStream fileOut = new FileOutputStream(CONFIG_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(cookies);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadCookies(WebDriver driver) {
        try {
            FileInputStream fileIn = new FileInputStream(CONFIG_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Set<Cookie> cookies = (Set<Cookie>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
