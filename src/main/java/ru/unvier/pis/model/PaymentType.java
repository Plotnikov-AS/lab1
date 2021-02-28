package ru.unvier.pis.model;

public enum PaymentType {
    CASH,
    CREDIT_CARD,
    CREDIT,
    BARTER,
    OFFSET;

    public static PaymentType parsePaymentType(String paymentType) {
        if (CASH.name().equalsIgnoreCase(paymentType)) return CASH;
        if (CREDIT_CARD.name().equalsIgnoreCase(paymentType)) return CREDIT_CARD;
        if (CREDIT.name().equalsIgnoreCase(paymentType)) return CREDIT;
        if (BARTER.name().equalsIgnoreCase(paymentType)) return BARTER;
        if (OFFSET.name().equalsIgnoreCase(paymentType)) return OFFSET;

        return null;
    }

}
