package com.transport.transport_system.service;

import org.springframework.stereotype.Service;

@Service
public class FareService {

    private final double PRICE_PER_KM = 2.0;
    private static final double TAX_RATE = 0.18;

    public double calculateFare(double distance, boolean isAC, boolean isPeak) {
        double fare = distance * PRICE_PER_KM;

        if (isAC) fare += fare * 0.2;
        if (isPeak) fare += fare * 0.1;

        return fare;
    }

    public double calculateBaseFarePerSeat(double distance) {
        return calculateFare(distance, false, false);
    }

    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    public double calculateTotalWithTax(double subtotal) {
        return subtotal + calculateTax(subtotal);
    }

    public double getTaxRate() {
        return TAX_RATE;
    }

    public double calculateRefund(double fare, int hoursBeforeDeparture) {
        if (hoursBeforeDeparture < 24) {
            return fare * 0.5;
        } else {
            return fare * 0.8;
        }
    }
}