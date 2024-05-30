package org.example.hilos.estructurados;

import java.util.HashMap;
import java.util.Map;

public class MetodosSynchronizedExample {
    public static void main(String[] args) {
        FlightBookingSystem bookingSystem = new FlightBookingSystem();

        Thread passenger1 = new Thread(new Passenger(bookingSystem, "Alice", "Vuelo 1", 2));
        Thread passenger2 = new Thread(new Passenger(bookingSystem, "Bob", "Vuelo 1", 7));
        Thread passenger3 = new Thread(new Passenger(bookingSystem, "Charlie", "Vuelo 2", 1));
        Thread passenger4 = new Thread(new Passenger(bookingSystem, "Diana", "Vuelo 2", 9));
        Thread passenger5 = new Thread(new Passenger(bookingSystem, "Kevin", "Vuelo 2", 1));
        Thread passenger6 = new Thread(new Passenger(bookingSystem, "Gus", "Vuelo 1", 2));

        passenger1.start();
        passenger2.start();
        passenger3.start();
        passenger4.start();
        passenger5.start();
        passenger6.start();
    }
}

class FlightBookingSystem {
    private Map<String, Integer> availableSeats;

    public FlightBookingSystem() {
        availableSeats = new HashMap<>();
        availableSeats.put("Vuelo 1", 10);
        availableSeats.put("Vuelo 2", 10);
    }

    public synchronized void bookSeat(String flightName, int numberOfSeats, String passengerName) {
        if (!availableSeats.containsKey(flightName)) {
            System.out.println("Nombre de vuelo no válido.");
            return;
        }

        int remainingSeats = availableSeats.get(flightName);
        if (remainingSeats >= numberOfSeats) {
            System.out.println(passengerName + " reservó " + numberOfSeats + " asientos en " + flightName);
            availableSeats.put(flightName, remainingSeats - numberOfSeats);
        } else {
            System.out.println("Lo sentimos, " + passengerName + ". No tenemos asientos suficientes en " + flightName);
        }
    }
}

class Passenger implements Runnable {
    private FlightBookingSystem bookingSystem;
    private String name;
    private String flightName;
    private int numberOfSeats;

    public Passenger(FlightBookingSystem bookingSystem, String name, String flightName, int numberOfSeats) {
        this.bookingSystem = bookingSystem;
        this.name = name;
        this.flightName = flightName;
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public void run() {
        bookingSystem.bookSeat(flightName, numberOfSeats, name);
    }
}
