package ru.unvier.pis.model;

import lombok.Getter;

public enum PaymentType {
    CASH("Наличный", "cash"),
    CREDIT_CARD("Безналичный", "creditCard"),
    BACK_RETURN("Возврат", "backReturn"),
    ALL("Все", "all");

    public final String cyrillicName;
    public final String engName;

    PaymentType(String cyrillicName, String engName) {
        this.cyrillicName = cyrillicName;
        this.engName = engName;
    }

    public static PaymentType parsePaymentType(String paymentType) {
        if (CASH.equals(paymentType))
            return CASH;
        if (CREDIT_CARD.equals(paymentType))
            return CREDIT_CARD;
        if (BACK_RETURN.equals(paymentType))
            return BACK_RETURN;
        if (ALL.equals(paymentType))
            return ALL;

        throw new RuntimeException("Cant parse payment type");
    }

    public boolean equals(String paymentType) {
        return this.name().equalsIgnoreCase(paymentType) || this.cyrillicName.equalsIgnoreCase(paymentType)
                || this.engName.equalsIgnoreCase(paymentType);
    }

}
