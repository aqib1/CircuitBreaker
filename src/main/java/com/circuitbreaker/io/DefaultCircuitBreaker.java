package com.circuitbreaker.io;

import java.util.function.Supplier;

public non-sealed class DefaultCircuitBreaker<T> implements CircuitBreaker<T> {

    private final Supplier<T> supplier;
    private boolean isOpen;
    private final long resetTimeout;
    private final int failureThreshold;
    private int currentFailureCount;
    private long lastTimeout;

    public DefaultCircuitBreaker(Supplier<T> supplier, long resetTimeout, int failureThreshold) {
        this.supplier = supplier;
        this.resetTimeout = resetTimeout;
        this.failureThreshold = failureThreshold;
        this.currentFailureCount = 0;
        this.lastTimeout = 0;
        this.isOpen = false;
    }

    @Override
    public T execute(Supplier<T> fallback) {
        if(isOpen() && (System.currentTimeMillis() - lastTimeout) >= this.resetTimeout) {
            close();
            currentFailureCount = 0;
        }

        if(isOpen()) {
            return fallback.get();
        }

        try {
            T result = this.supplier.get();
            currentFailureCount = 0;
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            currentFailureCount++;
            lastTimeout = System.currentTimeMillis();
            isOpen = this.currentFailureCount >= this.failureThreshold;
            return fallback.get();
        }
    }

    @Override
    public void open() {
        this.isOpen = true;
    }

    @Override
    public void close() {
        this.isOpen = false;
    }

    @Override
    public boolean isOpen() {
        return isOpen || currentFailureCount >= failureThreshold;
    }

    @Override
    public boolean isClose() {
        return false;
    }

    @Override
    public int getFailureThreshold() {
        return this.failureThreshold;
    }

    @Override
    public long getResetTimeout() {
        return this.resetTimeout;
    }

    @Override
    public long getLastTimeout() {
        return this.lastTimeout;
    }

    @Override
    public int getCurrentFailureCount() {
        return this.currentFailureCount;
    }
}
