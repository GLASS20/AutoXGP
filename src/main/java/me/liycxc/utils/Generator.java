package me.liycxc.utils;

import java.util.Random;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-07-01
 * @time: 22:43
 */
public class Generator {
    // 生成随机邮箱
    public static String generateRandomEmail() {
        return generateRandomString(8);
    }

    public static String generatePlayerId() {
        return "L" + generateRandomString(7);
    }

    // 生成随机密码
    public static String generateRandomPassword() {
        String[] characters = {"abcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "0123456789", "!@#$%^&*"};
        Random random = new Random();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            String characterSet = characters[random.nextInt(characters.length)];
            int randomIndex = random.nextInt(characterSet.length());
            password.append(characterSet.charAt(randomIndex));
        }

        return password.toString();
    }

    // 生成随机字符串
    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
}
