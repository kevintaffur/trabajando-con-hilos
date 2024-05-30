package org.example.hilos.noestructurados.locks;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityInversionExample {
    public static void main(String[] args) {
        Runway runway = new Runway();

        Runnable emergencyPlaneTask = () -> {
            Airplane emergencyPlane = new Airplane("Avión de Emergencia", 3);
            runway.requestLanding(emergencyPlane);
        };

        Runnable commercialPlaneTask = () -> {
            Airplane commercialPlane = new Airplane("Avión Comercial", 2);
            runway.requestLanding(commercialPlane);
        };

        Runnable privatePlaneTask = () -> {
            Airplane privatePlane = new Airplane("Avión Privado", 1);
            runway.requestLanding(privatePlane);
        };

        Thread emergencyPlane = new Thread(emergencyPlaneTask, "EmergencyPlaneThread");
        Thread commercialPlane = new Thread(commercialPlaneTask, "CommercialPlaneThread");
        Thread privatePlane = new Thread(privatePlaneTask, "PrivatePlaneThread");

        privatePlane.start();
        commercialPlane.start();
        emergencyPlane.start();
    }
}

class Airplane implements Comparable<Airplane> {
    private String name;
    private int priority;

    public Airplane(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Airplane other) {
        return Integer.compare(other.priority, this.priority);
    }
}

class Runway {
    private final Lock lock = new ReentrantLock(true); // Fair lock to handle priority inversion
    private final PriorityBlockingQueue<Airplane> queue = new PriorityBlockingQueue<>();

    public void requestLanding(Airplane airplane) {
        queue.add(airplane);
        while (true) {
            if (queue.peek().equals(airplane)) {
                lock.lock();
                try {
                    if (queue.peek().equals(airplane)) {
                        queue.poll();
                        System.out.println(airplane.getName() + " ha despegado.");
                        Thread.sleep(1000); // Simulate the time to land
                        System.out.println(airplane.getName() + " ha aterrizado.");
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
