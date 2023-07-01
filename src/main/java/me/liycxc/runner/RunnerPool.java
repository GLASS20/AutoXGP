package me.liycxc.runner;

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
public class RunnerPool {
    public static ArrayList<Thread> runners = new ArrayList<>();
    public static void createMicrosoft() {
        Thread thread = new Thread(new Runner());
        runners.add(thread);
        thread.start();
    }
}
