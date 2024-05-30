package org.example.hilos.noestructurados.semaforos;

import java.util.concurrent.Semaphore;

public class SemaforoBinarioExample {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot();

        Runnable enterTask = () -> {
            String vehicleName = Thread.currentThread().getName();
            parkingLot.enter(vehicleName);
        };

        Runnable exitTask = () -> {
            String vehicleName = Thread.currentThread().getName();
            parkingLot.exit(vehicleName);
        };

        Thread vehicle1 = new Thread(enterTask, "Vehículo 1");
        Thread vehicle2 = new Thread(exitTask, "Vehículo 2");
        Thread vehicle3 = new Thread(enterTask, "Vehículo 3");
        Thread vehicle4 = new Thread(exitTask, "Vehículo 4");

        vehicle1.start();
        vehicle2.start();
        vehicle3.start();
        vehicle4.start();
    }
}

class ParkingLot {
    private final Semaphore semaphore = new Semaphore(1); // Semaphore binario

    public void enter(String vehicleName) {
        try {
            semaphore.acquire(); // Adquirir el permiso
            System.out.println(vehicleName + " está entrando al estacionamiento.");
            Thread.sleep(1000); // Simular el tiempo de entrada
            System.out.println(vehicleName + " ha entrado al estacionamiento.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release(); // Liberar el permiso
        }
    }

    public void exit(String vehicleName) {
        try {
            semaphore.acquire(); // Adquirir el permiso
            System.out.println(vehicleName + " está saliendo del estacionamiento.");
            Thread.sleep(1000); // Simular el tiempo de salida
            System.out.println(vehicleName + " ha salido del estacionamiento.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release(); // Liberar el permiso
        }
    }
}
