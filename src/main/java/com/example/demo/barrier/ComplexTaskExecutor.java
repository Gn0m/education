package com.example.demo.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ComplexTaskExecutor implements Runnable {

    private final ExecutorService executorService;
    private final Random r = new Random();

    public ComplexTaskExecutor() {
        executorService = Executors.newCachedThreadPool();
    }

    public void executeTasks(int numberOfTasks) {
        CyclicBarrier barrier = new CyclicBarrier(numberOfTasks, this);
        for (int i = 0; i < numberOfTasks; i++) {
            executorService.execute(new ComplexTask(barrier));
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(24L, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    @Override
    public void run() {
        log.info("Consolidation of operations, sum all operations: " + r.nextInt(100));
    }
}
