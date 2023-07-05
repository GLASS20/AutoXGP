package me.liycxc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date:  2023-06-30
 * @time:  22:09
 */
@SpringBootApplication
public class AppMain {
    public static String API_MAIL_TOKEN;
    public static String API_ALIPAY_PAYKEY;
    public static String DRIVER_DATA;
    public static void main(String[] args) {
        API_MAIL_TOKEN = args[0];
        API_ALIPAY_PAYKEY = args[1];
        DRIVER_DATA = args[2];
        SpringApplication.run(AppMain.class, args);
    }
}
