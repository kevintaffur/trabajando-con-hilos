package org.example.hilos.noestructurados.barreras;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        HikingTrail hikingTrail = new HikingTrail();

        Runnable hikerTask = () -> {
            String hikerName = Thread.currentThread().getName();
            hikingTrail.hike(hikerName);
        };

        Thread hiker1 = new Thread(hikerTask, "Excursionista 1");
        Thread hiker2 = new Thread(hikerTask, "Excursionista 2");
        Thread hiker3 = new Thread(hikerTask, "Excursionista 3");
        Thread hiker4 = new Thread(hikerTask, "Excursionista 4");
        Thread hiker5 = new Thread(hikerTask, "Excursionista 5");

        hiker1.start();
        hiker2.start();
        hiker3.start();
        hiker4.start();
        hiker5.start();
    }
}

class HikingTrail {
    private final int numberOfHikers = 5; // Número total de excursionistas
    private final CyclicBarrier meetingPoint = new CyclicBarrier(numberOfHikers, () -> {
        System.out.println("Todos los excursionistas han alcanzado el punto de encuentro. Continuarán juntos!");
    });

    public void hike(String hikerName) {
        try {
            System.out.println(hikerName + " está haciendo el hiking en la ruta.");
            Thread.sleep(2000); // Simulate time taken to hike
            System.out.println(hikerName + " ha alcanzado el punto de encuentro.");
            meetingPoint.await(); // Esperar a que todos los excursionistas alcancen el punto de encuentro
            System.out.println(hikerName + " continúa el hiking con el grupo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
