package com.transport.transport_system.strategy;

public class WalletPayment implements PaymentStrategy {
    
    private String walletId;
    
    public WalletPayment(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate payment gateway logic for Digital Wallet
        System.out.println("Processing Wallet payment of $" + amount + " from Wallet ID: " + walletId);
        return true; // Assume success for simulation
    }

    @Override
    public String getPaymentMethodName() {
        return "Wallet";
    }
}
