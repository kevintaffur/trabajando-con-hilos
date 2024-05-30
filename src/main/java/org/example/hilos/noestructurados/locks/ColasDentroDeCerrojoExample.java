package org.example.hilos.noestructurados.locks;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ColasDentroDeCerrojoExample {
    public static void main(String[] args) {
        CoffeeShop coffeeShop = new CoffeeShop();

        Runnable customerTask = () -> {
            String[] orders = {"Espresso", "Latte", "Cappuccino", "Americano"};
            for (String order : orders) {
                coffeeShop.placeOrder(order);
                try {
                    Thread.sleep(500); // Simulate time between placing orders
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable baristaTask = () -> {
            String baristaName = Thread.currentThread().getName();
            while (true) {
                coffeeShop.prepareOrder(baristaName);
            }
        };

        Thread customerThread = new Thread(customerTask, "Cliente");
        Thread barista1 = new Thread(baristaTask, "Barista 1");
        Thread barista2 = new Thread(baristaTask, "Barista 2");

        customerThread.start();
        barista1.start();
        barista2.start();
    }
}

class CoffeeShop {
    private final Lock lock = new ReentrantLock();
    private final Condition newOrderCondition = lock.newCondition();
    private final Queue<String> orderQueue = new LinkedList<>();

    public void placeOrder(String order) {
        lock.lock();
        try {
            orderQueue.add(order);
            System.out.println("Orden hecha: " + order);
            newOrderCondition.signal(); // Signal baristas that a new order is available
        } finally {
            lock.unlock();
        }
    }

    public void prepareOrder(String baristaName) {
        lock.lock();
        try {
            while (orderQueue.isEmpty()) {
                try {
                    System.out.println(baristaName + " está esperando órdenes...");
                    newOrderCondition.await(); // Wait for a new order
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            String order = orderQueue.poll();
            System.out.println(baristaName + " está preparando: " + order);
            Thread.sleep(1000); // Simulate time taken to prepare the order
            System.out.println(baristaName + " ha terminado de preparar: " + order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}