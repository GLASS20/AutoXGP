package me.liycxc.runner;

import me.liycxc.microsoft.Microsoft;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-07-01
 * @time: 22:09
 */
public class Runner implements Runnable {
    @Override
    public void run() {
        Driver.driver.manage().deleteAllCookies();
        String[] account = Microsoft.createAccount();
    }
}

