package com.circuitbreaker.io;

import java.util.function.Supplier;

public sealed interface CircuitBreaker<T> permits DefaultCircuitBreaker {
    T execute(Supplier<T> fallback);
    void open();
    void close();
    boolean isOpen();
    boolean isClose();
    int getFailureThreshold();
    long getResetTimeout();
    long getLastTimeout();
    int getCurrentFailureCount();

}
