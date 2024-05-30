package org.example.hilos.noestructurados.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VariablesDeCondicionExample {
    public static void main(String[] args) {
        Repository repository = new Repository();

        Runnable commitTask = () -> {
            String developerName = Thread.currentThread().getName();
            repository.commitCode(developerName);
        };

        Runnable pullTask = () -> {
            String developerName = Thread.currentThread().getName();
            repository.pullCode(developerName);
        };

        Thread developer1 = new Thread(commitTask, "Desarrollador 1");
        Thread developer2 = new Thread(pullTask, "Desarrollador 2");
        Thread developer3 = new Thread(commitTask, "Desarrollador 3");
        Thread developer4 = new Thread(pullTask, "Desarrollador 3");

        developer1.start();
        developer2.start();
        developer3.start();
        developer4.start();
    }
}

class Repository {
    private final Lock lock = new ReentrantLock();
    private final Condition commitCondition = lock.newCondition();
    private boolean isCommitting = false;

    public void commitCode(String developerName) {
        lock.lock();
        try {
            while (isCommitting) {
                try {
                    commitCondition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            isCommitting = true;
            System.out.println(developerName + " está haciendo un commit de código.");
            Thread.sleep(1000); // Simulate the time taken to commit code
            System.out.println(developerName + " ha finalizado el commit de código.");
            isCommitting = false;
            commitCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void pullCode(String developerName) {
        lock.lock();
        try {
            while (isCommitting) {
                try {
                    commitCondition.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(developerName + " está haciendo un pull de código.");
            Thread.sleep(500); // Simulate the time taken to pull code
            System.out.println(developerName + " ha terminado el pull de código.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}

