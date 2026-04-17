package com.transport.transport_system.strategy;

public interface PaymentStrategy {
    boolean pay(double amount);
    String getPaymentMethodName();
}
