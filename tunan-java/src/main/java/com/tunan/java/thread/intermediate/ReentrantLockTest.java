package com.tunan.java.thread.intermediate;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    public static void main(String[] args) {
        ReentrantLock lock  = new ReentrantLock();

        for (int i = 0; i < 3; i++) {
            lock.lock();
        }

        for (int i = 0; i < 3; i++) {
            try {

            } finally {
                lock.unlock();
            }
        }
    }
}
