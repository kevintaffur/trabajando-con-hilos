package org.example.hilos.estructurados;

import java.util.ArrayList;
import java.util.List;

public class BloquesSynchronizedExample {
    public static void main(String[] args) {
        Cafe cafe = new Cafe();

        List<Thread> customers = new ArrayList<>();
        customers.add(new Thread(new Customer(cafe, "Alice", "coffee")));
        customers.add(new Thread(new Customer(cafe, "Bob", "cake")));
        customers.add(new Thread(new Customer(cafe, "Charlie", "coffee")));
        customers.add(new Thread(new Customer(cafe, "Diana", "cake")));
        customers.add(new Thread(new Customer(cafe, "Kevin", "coffee")));
        customers.add(new Thread(new Customer(cafe, "Gus", "cake")));
        customers.add(new Thread(new Customer(cafe, "Anna", "coffee")));
        customers.add(new Thread(new Customer(cafe, "Pedro", "cake")));

        for (Thread customer : customers) {
            customer.start();
        }
    }
}

class Cafe {
    private int coffeeStock = 3;
    private int cakeStock = 3;

    public synchronized void takeOrder(String customerName, String item) {
        if (item.equals("coffee")) {
            if (coffeeStock > 0) {
                System.out.println(customerName + " ha ordenado un café.");
                coffeeStock--;
            } else {
                System.out.println("Lo sentimos, " + customerName + ". Nos hemos quedado sin café.");
            }
        } else if (item.equals("cake")) {
            if (cakeStock > 0) {
                System.out.println(customerName + " ha ordenado un pastel.");
                cakeStock--;
            } else {
                System.out.println("Lo sentimos, " + customerName + ". Nos hemos quedado sin pastel.");
            }
        }
    }
}

class Customer implements Runnable {
    private final Cafe cafe;
    private final String name;
    private final String item;

    public Customer(Cafe cafe, String name, String item) {
        this.cafe = cafe;
        this.name = name;
        this.item = item;
    }

    @Override
    public void run() {
        cafe.takeOrder(name, item);
    }
}
