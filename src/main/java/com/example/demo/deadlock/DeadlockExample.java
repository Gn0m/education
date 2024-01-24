package com.example.demo.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class DeadlockExample {

    private static class Resource {
    }

    private final Resource resourceA = new Resource();
    private final Resource resourceB = new Resource();
    private final ReentrantLock lockA = new ReentrantLock();
    private final ReentrantLock lockB = new ReentrantLock();
    private final Comparator<ReentrantLock> comparator = Comparator.comparingInt(Object::hashCode);

    public void execute() {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                acquireResourcesAndWork(lockA, lockB, resourceA, resourceB, "Thread-1", comparator);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                acquireResourcesAndWork(lockB, lockA, resourceB, resourceA, "Thread-2", comparator); //переставить местами
            }
        });

        thread1.start();
        thread2.start();
    }

    private void acquireResourcesAndWork(ReentrantLock firstLock, ReentrantLock secondLock,
                                         Resource firstResource, Resource secondResource, String threadName, Comparator<ReentrantLock> comparator) {
        
        ReentrantLock innerFirstLock = firstLock;
        ReentrantLock innerSecondLock = secondLock;
        //проверяем если переданны в разном порядке меняем их местами
        if (comparator.compare(firstLock, secondLock) < 0) {
            innerFirstLock = secondLock;
            innerSecondLock = firstLock;
        }
        innerFirstLock.lock();
        log.info(threadName + " locked " + firstResource + " first");
        try {
            // Имитация работы с ресурсом
            Thread.sleep(100);

            innerSecondLock.lock();
            log.info(threadName + " locked " + secondResource + " second");

            inner(secondResource, threadName, innerSecondLock);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            innerFirstLock.unlock();
            log.info(threadName + " unlocked " + firstResource);
        }
    }

    private static void inner(Resource secondResource, String threadName, ReentrantLock innerSecondLock) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            innerSecondLock.unlock();
            log.info(threadName + " unlocked " + secondResource);
        }
    }

    public static void main(String[] args) {
        DeadlockExample example = new DeadlockExample();
        example.execute();
    }
}

