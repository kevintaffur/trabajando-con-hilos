package org.example.hilos.noestructurados.locks;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    public static void main(String[] args) {
        Printer printer = new Printer();

        Runnable urgentDocumentTask = () -> {
            Document urgentDocument = new Document("Documento Urgente", 3);
            printer.requestPrint(urgentDocument);
        };

        Runnable normalDocumentTask = () -> {
            Document normalDocument = new Document("Documento Normal", 2);
            printer.requestPrint(normalDocument);
        };

        Runnable lowPriorityDocumentTask = () -> {
            Document lowPriorityDocument = new Document("Documento de Prioridad Baja", 1);
            printer.requestPrint(lowPriorityDocument);
        };

        Thread urgentDocument = new Thread(urgentDocumentTask, "UrgentDocumentThread");
        Thread normalDocument = new Thread(normalDocumentTask, "NormalDocumentThread");
        Thread lowPriorityDocument = new Thread(lowPriorityDocumentTask, "LowPriorityDocumentThread");

        lowPriorityDocument.start();
        normalDocument.start();
        urgentDocument.start();
    }
}

class Document implements Comparable<Document> {
    private String name;
    private int priority;

    public Document(String name, int priority) {
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
    public int compareTo(Document other) {
        return Integer.compare(other.priority, this.priority);
    }
}

class Printer {
    private final Lock lock = new ReentrantLock();
    private final PriorityBlockingQueue<Document> queue = new PriorityBlockingQueue<>();

    public void requestPrint(Document document) {
        queue.add(document);
        while (true) {
            if (queue.peek().equals(document)) {
                lock.lock();
                try {
                    if (queue.peek().equals(document)) {
                        queue.poll();
                        System.out.println(document.getName() + " está imprimiendo.");
                        Thread.sleep(1000); // Simulate the time to print
                        System.out.println(document.getName() + " ha terminado de imprimir.");
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
