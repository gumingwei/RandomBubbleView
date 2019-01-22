package com.yuntu.randombubbleview.msgq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class Queue {

    public BlockingQueue queue = new LinkedBlockingDeque();

    public Queue() {
    }

    public void put(Object arg) {
        try {
            queue.put(arg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Object take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object peek() {
        return queue.peek();
    }

    public void clear() {
        queue.clear();
    }

    public void isEmpty() {
        queue.isEmpty();
    }

    public abstract void stop();
}
