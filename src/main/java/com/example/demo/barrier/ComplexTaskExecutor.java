package com.example.demo.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ComplexTaskExecutor implements Runnable {

    private final Random r = new Random();
    private final CyclicBarrier barrier;

    public ComplexTaskExecutor(int numberOfTasks) {
        barrier = new CyclicBarrier(numberOfTasks, this);
    }

    public void executeTasks(int numberOfTasks) {

        try(ExecutorService executorService = Executors.newFixedThreadPool(numberOfTasks)){
            for (int i = 0; i < numberOfTasks; i++) {
                executorService.execute(new ComplexTask(barrier));
            }
        }
    }

    @Override
    public void run() {
        log.info("Consolidation of operations, sum all operations: " + r.nextInt(100));
    }
}
