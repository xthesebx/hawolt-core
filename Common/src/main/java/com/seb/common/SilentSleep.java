package com.seb.common;

public class SilentSleep {
    public static void sleep(int x) {
        try {
            Thread.sleep(x);
        } catch (InterruptedException ignored) {
        }
    }
}
