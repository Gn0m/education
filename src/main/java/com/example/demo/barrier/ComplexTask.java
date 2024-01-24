package com.example.demo.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class ComplexTask implements Runnable {

    private final CyclicBarrier barrier;
    private final Random r = new Random();

    public ComplexTask(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    private void execute() throws InterruptedException {
        Thread.sleep(r.nextInt(500,1000));
        try {
            log.info(Thread.currentThread().getName() + " awaits other threads");
            barrier.await();
            log.info(Thread.currentThread().getName() + " AWAKING");
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
