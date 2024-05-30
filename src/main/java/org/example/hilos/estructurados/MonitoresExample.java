package org.example.hilos.estructurados;

import java.util.HashSet;
import java.util.Set;

public class MonitoresExample {
    public static void main(String[] args) {
        LibraryMonitor library = new LibraryMonitor();

        Thread student1 = new Thread(new Student(library, "Alice", "Introduction to Java"));
        Thread student2 = new Thread(new Student(library, "Bob", "Data Structures and Algorithms"));
        Thread student3 = new Thread(new Student(library, "Charlie", "Operating Systems"));

        Thread student4 = new Thread(new Student(library, "Kevin", "Introduction to Java"));
        Thread student5 = new Thread(new Student(library, "Gus", "Data Structures and Algorithms"));
        Thread student6 = new Thread(new Student(library, "Maria", "Operating Systems"));

        student1.start();
        student2.start();
        student3.start();

        student4.start();
        student5.start();
        student6.start();
    }
}

class LibraryMonitor {
    private Set<String> borrowedBooks = new HashSet<>();

    public synchronized void borrowBook(String studentName, String bookName) throws InterruptedException {
        while (borrowedBooks.contains(bookName)) {
            wait(); // Esperar hasta que el libro esté disponible
        }
        borrowedBooks.add(bookName);
        System.out.println(studentName + " tomó prestado el libro: " + bookName);
    }

    public synchronized void returnBook(String studentName, String bookName) {
        borrowedBooks.remove(bookName);
        System.out.println(studentName + " devolvió el libro: " + bookName);
        notifyAll(); // Notificar a los otros estudiantes que el libro ha sido devuelto
    }
}

class Student implements Runnable {
    private final LibraryMonitor library;
    private final String name;
    private final String bookName;

    public Student(LibraryMonitor library, String name, String bookName) {
        this.library = library;
        this.name = name;
        this.bookName = bookName;
    }

    @Override
    public void run() {
        try {
            library.borrowBook(name, bookName);
            Thread.sleep(2000); // Simular tiempo de lectura del libro
            library.returnBook(name, bookName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
