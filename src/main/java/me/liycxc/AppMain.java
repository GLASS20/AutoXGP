package me.liycxc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-06-30
 * @time: 22:09
 */
@SpringBootApplication
public class AppMain {
    public static String API_HTTP_TOKEN;
    public static String API_MAIL_TOKEN;
    public static String API_ALIPAY_USERNAME;
    public static String API_ALIPAY_PASSWORD;
    public static String API_ALIPAY_PAYKEY;
    public static String DRIVER_DATA;
    public static int DRIVER_WAIT;
    public static int DRIVER_WAIT_ERROR;
    public static boolean DRIVER_HEADLESS;

    public static void main(String[] args) {
        try {
            API_MAIL_TOKEN = args[0];
            API_ALIPAY_USERNAME = args[1];
            API_ALIPAY_PASSWORD = args[2];
            API_ALIPAY_PAYKEY = args[3];
            DRIVER_DATA = args[4];
            API_HTTP_TOKEN = args[5];
            DRIVER_WAIT = Integer.parseInt(args[6]);
            DRIVER_WAIT_ERROR = Integer.parseInt(args[7]);
            DRIVER_HEADLESS = Boolean.parseBoolean(args[8]);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        SpringApplication.run(AppMain.class, args);
    }
}
