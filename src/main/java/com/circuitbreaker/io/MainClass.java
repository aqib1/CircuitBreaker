package com.circuitbreaker.io;

public class MainClass {

    public static void main(String[] args) {
        CircuitBreaker<String> circuitBreaker =
                new DefaultCircuitBreaker<>(
                        () -> "Hi there",
                        2000,
                        2);

        System.out.println(circuitBreaker.execute(() -> "Fallback"));
    }
}
