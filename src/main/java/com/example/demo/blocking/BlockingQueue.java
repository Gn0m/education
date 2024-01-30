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
    private final Object enqueue = new Object();
    private final Object dequeue = new Object();


    public void enqueue() {

        synchronized (enqueue) {
            while (size >= 5) {
                try {
                    log.info(Thread.currentThread().getName() + " awaits");
                    enqueue.wait();
                } catch (InterruptedException ex) {
                    log.warn("Interrupted ", ex);
                    Thread.currentThread().interrupt();
                }
            }
        }
        synchronized (dequeue) {
            Task task = new Task();
            log.info(task + " added");
            tasks[put] = task;
            put++;
            size++;

            if (put == 5) {
                put = 0;
            }
            dequeue.notifyAll();
        }
    }


    @SneakyThrows
    public void dequeue() {
        Task task;

        synchronized (dequeue) {
            while (size < 1) {
                try {
                    log.info(Thread.currentThread().getName() + " awaits");
                    dequeue.wait();
                } catch (InterruptedException ex) {
                    log.warn("Interrupted: ", ex);
                    Thread.currentThread().interrupt();
                }
            }
        }
        synchronized (enqueue) {
            task = tasks[get];
            log.info(task + " received");
            tasks[get] = null;
            get++;
            size--;

            if (get == 5)
                get = 0;
            enqueue.notifyAll();
        }
    }


    public int size() {
        return size;
    }
}

