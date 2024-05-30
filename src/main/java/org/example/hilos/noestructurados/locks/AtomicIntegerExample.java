package org.example.hilos.noestructurados.locks;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {
    private AtomicInteger ticketsAvailable;

    public AtomicIntegerExample(int initialTickets) {
        this.ticketsAvailable = new AtomicInteger(initialTickets);
    }

    public boolean bookTicket() {
        while (true) {
            int currentTickets = ticketsAvailable.get();
            if (currentTickets > 0) {
                // Intenta decrementar el número de boletos disponibles de forma atómica
                // Simulate acquire lock
                if (ticketsAvailable.compareAndSet(currentTickets, currentTickets - 1)) {
                    // Simulate release lock
                    System.out.println(Thread.currentThread().getName() + " reservó un ticket. Tickets disponibles: " + (currentTickets - 1));
                    return true;
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " falló al intentar reservar un ticket. No hay tickets disponibles.");
                return false;
            }
        }
    }

    public static void main(String[] args) {
        AtomicIntegerExample ticketBooking = new AtomicIntegerExample(10);

        Runnable bookTask = () -> {
            for (int i = 0; i < 3; i++) {  // Cada hilo intenta reservar 3 tickets
                ticketBooking.bookTicket();
                try {
                    Thread.sleep(50); // Simula un delay entre los intentos de reserva
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread user1 = new Thread(bookTask, "Usuario 1");
        Thread user2 = new Thread(bookTask, "Usuario 2");
        Thread user3 = new Thread(bookTask, "Usuario 3");

        user1.start();
        user2.start();
        user3.start();
    }
}
