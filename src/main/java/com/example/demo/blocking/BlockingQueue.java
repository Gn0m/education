package com.example.demo.blocking;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlockingQueue {

    private static final int LENGTH = 5;
    private int size = 0;
    private int get = 0;
    private int put = 0;
    private final Task[] tasks = new Task[LENGTH];


    public synchronized void enqueue() {

        while (size >= 5) {
            try {
                log.info(Thread.currentThread().getName() + " awaits");
                wait();
            } catch (InterruptedException ex) {
                log.warn("Interrupted ", ex);
                Thread.currentThread().interrupt();
            }
        }

        Task task = new Task();
        log.info(task + " added");
        tasks[put] = task;
        put++;
        size++;

        if (put == 5) {
            put = 0;
        }
        notifyAll();
    }


    @SneakyThrows
    public synchronized void dequeue() {
        Task task;

        while (size < 1) {
            try {
                log.info(Thread.currentThread().getName() + " awaits");
                wait();
            } catch (InterruptedException ex) {
                log.warn("Interrupted: ", ex);
                Thread.currentThread().interrupt();
            }
        }

        task = tasks[get];
        log.info(task + " received");
        tasks[get] = null;
        get++;
        size--;

        if (get == 5)
            get = 0;
        notifyAll();
    }


    public int size() {
        return size;
    }
}

