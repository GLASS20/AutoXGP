package me.liycxc.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.json.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

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
    private static final String CONFIG_FILE_PATH = "cookies.json";

    public static void saveCookies(WebDriver driver) {
        List<Map<String, Object>> cookieList = new ArrayList<>();
        Set<Cookie> cookies = driver.manage().getCookies();

        for (Cookie cookie : cookies) {
            Map<String, Object> cookieMap = new HashMap<>();
            cookieMap.put("name", cookie.getName());
            cookieMap.put("value", cookie.getValue());
            cookieMap.put("domain", cookie.getDomain());
            cookieMap.put("path", cookie.getPath());
            cookieMap.put("expiry", cookie.getExpiry().getTime());
            cookieMap.put("isSecure", cookie.isSecure());
            cookieMap.put("isHttpOnly", cookie.isHttpOnly());
            cookieMap.put("sameSite", cookie.getSameSite());

            cookieList.add(cookieMap);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(CONFIG_FILE_PATH)) {
            gson.toJson(cookieList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadCookies(WebDriver driver) {
        List<Map<String, Object>> cookieList;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Reader reader = new FileReader(CONFIG_FILE_PATH)) {
            Type type = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            cookieList = gson.fromJson(reader, type);

            for (Map<String, Object> cookieMap : cookieList) {
                Cookie cookie = new Cookie(
                        (String) cookieMap.get("name"),
                        (String) cookieMap.get("value"),
                        (String) cookieMap.get("domain"),
                        (String) cookieMap.get("path"),
                        ("null".equals(cookieMap.get("expiry").toString()) ? null : new Date(Long.parseLong(cookieMap.get("expiry").toString()))),
                        (boolean) cookieMap.get("isSecure"),
                        (boolean) cookieMap.get("isHttpOnly"),
                        (String) cookieMap.get("sameSite")
                );

                driver.manage().addCookie(cookie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
