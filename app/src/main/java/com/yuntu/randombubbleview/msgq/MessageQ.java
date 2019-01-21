package com.yuntu.randombubbleview.msgq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class MessageQ {

    public BlockingQueue queue = new LinkedBlockingDeque<String>();

    public MessageQ() {
    }

    public void put(String arg) {
        try {
            queue.put(arg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String take() {
        try {
            return (String) queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String peek() {
        return (String) queue.peek();
    }

    public void clear() {
        queue.clear();
    }

    public void isEmpty() {
        queue.isEmpty();
    }

    public abstract void stop();
}
