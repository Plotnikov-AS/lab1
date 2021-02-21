package ru.unvier.pis.model;

import static java.lang.String.format;
import static ru.unvier.pis.constants.Constants.ErrorMessages.NOTHING_WAS_FOUND_ERROR;
import static ru.unvier.pis.constants.Constants.ParamNames.PAYMENT_TYPE;

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

        throw new RuntimeException(format(NOTHING_WAS_FOUND_ERROR, PAYMENT_TYPE, paymentType));
    }

}
