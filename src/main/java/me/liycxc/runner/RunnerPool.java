package me.liycxc.runner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

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
public class RunnerPool {
    public static ArrayList<Thread> runners = new ArrayList<>();
    @GetMapping("/xgp")
    public static void createMicrosoft() {
        if (Driver.driver == null) {
            Driver.init();
        }
        Thread thread = new Thread(new Runner());
        runners.add(thread);
        thread.start();
    }
}