package com.sofka.challenge.account_transaction.domain.enums;

public enum TransactionType {
    DEPOSIT {
        @Override
        public boolean isValidAmount(double amount) {
            return amount > 0;
        }
    },
    WITHDRAWAL {
        @Override
        public boolean isValidAmount(double amount) {
            return amount < 0;
        }
    };

    public abstract boolean isValidAmount(double amount);
}


