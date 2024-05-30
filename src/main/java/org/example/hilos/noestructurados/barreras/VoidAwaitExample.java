package org.example.hilos.noestructurados.barreras;

import java.util.concurrent.CountDownLatch;

public class VoidAwaitExample {
    public static void main(String[] args) throws InterruptedException {
        int numberOfRunners = 5;
        CountDownLatch finishLine = new CountDownLatch(numberOfRunners);

        for (int i = 1; i <= numberOfRunners; i++) {
            Thread runner = new Thread(new Runner(finishLine, "Runner " + i));
            runner.start();
        }

        finishLine.await(); // Wait for all runners to finish

        System.out.println("Todos los corredores han terminado la carrera. Los resultados pueden ser mostrados...");
    }
}

class Runner implements Runnable {
    private final CountDownLatch finishLine;
    private final String name;

    public Runner(CountDownLatch finishLine, String name) {
        this.finishLine = finishLine;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " ha empezado la carrera.");
            Thread.sleep((long) (Math.random() * 5000)); // Simulating running time
            System.out.println(name + " ha terminado la carrera.");
            finishLine.countDown(); // Signal that this runner has finished
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
