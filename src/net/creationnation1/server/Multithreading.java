package net.creationnation1.server;

class Multithreading {
    static void runTask(Runnable task) {
        new Thread(task).start();
    }
}
