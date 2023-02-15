package com.willy.myapplication.sequence;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceGenerator {
    private AtomicLong value = new AtomicLong(1);

    public int getNext() {
        return (int) value.getAndIncrement();
    }
}
