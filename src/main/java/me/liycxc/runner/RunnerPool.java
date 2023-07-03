package me.liycxc.runner;

import org.openqa.selenium.chrome.ChromeDriver;
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
    public static ArrayList<Runner> runners = new ArrayList<>();
    @GetMapping("/xgp")
    public static void createMicrosoft() {
        Runner thread = new Runner();
        runners.add(thread);
        thread.start();
    }
}
