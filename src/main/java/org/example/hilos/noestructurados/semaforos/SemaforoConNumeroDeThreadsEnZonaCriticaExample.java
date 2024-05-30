package org.example.hilos.noestructurados.semaforos;

import java.util.concurrent.Semaphore;

public class SemaforoConNumeroDeThreadsEnZonaCriticaExample {
    public static void main(String[] args) {
        int numberOfTables = 3; // Número de mesas disponibles en la biblioteca
        Library library = new Library(numberOfTables);

        Runnable studentTask = () -> {
            String studentName = Thread.currentThread().getName();
            library.useTable(studentName);
        };

        Thread student1 = new Thread(studentTask, "Estudiante 1");
        Thread student2 = new Thread(studentTask, "Estudiante 2");
        Thread student3 = new Thread(studentTask, "Estudiante 3");
        Thread student4 = new Thread(studentTask, "Estudiante 4");
        Thread student5 = new Thread(studentTask, "Estudiante 5");

        student1.start();
        student2.start();
        student3.start();
        student4.start();
        student5.start();
    }
}

class Library {
    private final Semaphore tables;

    public Library(int numberOfTables) {
        this.tables = new Semaphore(numberOfTables);
    }

    public void useTable(String studentName) {
        try {
            System.out.println(studentName + " está intentando conseguir una mesa.");
            tables.acquire(); // Adquirir un permiso para usar una mesa
            System.out.println(studentName + " ha conseguido una mesa.");
            Thread.sleep(2000); // Simular el tiempo de uso de la mesa
            System.out.println(studentName + " está dejando la mesa.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tables.release(); // Liberar el permiso
            System.out.println(studentName + " ha dejado la mesa.");
        }
    }
}
