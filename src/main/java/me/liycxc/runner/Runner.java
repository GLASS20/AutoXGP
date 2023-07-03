package me.liycxc.runner;

import me.liycxc.microsoft.Mail;
import me.liycxc.microsoft.Microsoft;
import me.liycxc.utils.Logger;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-07-01
 * @time: 22:09
 */
public class Runner extends Thread {
    @Override
    public void run() {
        ChromeDriver driver = Driver.getDriver();
        driver.manage().deleteAllCookies();
        String[] account = new String[]{"metelngonyar@hotmail.com","Gn37ms56"}; // Mail.getMailByApi();
        if (account == null) {
            return;
        }
        System.out.println("Login " + account[0] + " " + account[1]);
        HashMap<Boolean, String> loginValue = Microsoft.loginAccount(driver, account);
        for (Map.Entry<Boolean, String> i : loginValue.entrySet()) {
            if (!i.getKey()) {
                System.out.println("Login " + account[0] + " " + account[1] + " error: " + i.getValue());
            }
            return;
        }
    }
}

