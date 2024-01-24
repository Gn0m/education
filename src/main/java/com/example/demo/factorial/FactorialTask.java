package com.example.demo.factorial;

import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<Integer> {

    private final int n;

    public FactorialTask(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return 1;
        }

        FactorialTask task = new FactorialTask(n - 1);
        task.fork();

        return n * task.join();
    }
}
