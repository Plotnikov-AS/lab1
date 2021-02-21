package ru.unvier.pis.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.unvier.pis.model.PaymentType;

import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static ru.unvier.pis.constants.Constants.Config.APPLICATION_CONFIG_PREFIX;
import static ru.unvier.pis.constants.Constants.ErrorMessages.NOTHING_WAS_FOUND_ERROR;
import static ru.unvier.pis.constants.Constants.ErrorMessages.PARAM_IS_EMPTY_ERROR;
import static ru.unvier.pis.constants.Constants.ParamNames.PAYMENT_TYPE;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(APPLICATION_CONFIG_PREFIX)
public class ApplicationConfiguration {
    private final Map<String, String> paymentTypes;
    private final Map<String, String> result;

    public String getPaymentTypeName(PaymentType paymentType) {
        if(isNull(paymentType))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, PAYMENT_TYPE));

        return paymentTypes.get(paymentType.name());
    }

    public PaymentType parsePaymentType(String paymentTypeStr) {
        if(isNull(paymentTypeStr))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, PAYMENT_TYPE));

        PaymentType paymentType = null;
        if (paymentTypes.containsValue(paymentTypeStr)) {
            for (Map.Entry<String, String> entry : paymentTypes.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.equalsIgnoreCase(paymentTypeStr)) {
                    paymentType = PaymentType.parsePaymentType(key);
                }
            }
        }
        if (isNull(paymentType))
            throw new RuntimeException(format(NOTHING_WAS_FOUND_ERROR, PAYMENT_TYPE, paymentTypeStr));

        return paymentType;
    }
}
