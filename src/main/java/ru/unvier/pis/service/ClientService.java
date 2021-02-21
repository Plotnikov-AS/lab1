package ru.unvier.pis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.nio.sctp.IllegalReceiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unvier.pis.configuration.ApplicationConfiguration;
import ru.unvier.pis.dao.ClientDao;
import ru.unvier.pis.model.entity.Client;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.unvier.pis.constants.Constants.ErrorMessages.PARAM_IS_EMPTY_ERROR;
import static ru.unvier.pis.constants.Constants.ParamNames.CLIENT;
import static ru.unvier.pis.constants.Constants.ParamNames.ID;
import static ru.unvier.pis.model.PaymentType.*;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientDao clientDao;
    private final ApplicationConfiguration configuration;
    private final ObjectMapper objectMapper;

    public Client getClient(Long id) {
        if (isNull(id))
            throw new IllegalReceiveException(format(PARAM_IS_EMPTY_ERROR, ID));
        return clientDao.getClientById(id);
    }

    public List<Client> getClients(String clientStr) {
        if (isEmpty(clientStr))
            throw new IllegalReceiveException(format(PARAM_IS_EMPTY_ERROR, CLIENT));

        try {
            Client client = objectMapper.readValue(clientStr, Client.class);
            List<Client> clients = new ArrayList<>();
            if (!isEmpty(client.getLastName())) {
                clients = clientDao.getClientsByLastName(client.getLastName());
            } else if (!isEmpty(client.getFirstName())) {
                clients = clientDao.getClientsByFirstName(client.getFirstName());
            } else if (!isEmpty(client.getMiddleName())) {
                clients = clientDao.getClientsByMiddleName(client.getMiddleName());
            } else if (!isEmpty(client.getOrganizationName())) {
                clients = clientDao.getClientsByOrganizationName(client.getOrganizationName());
            }
            return clients;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Client addClient(String clientStr) {
        if (isEmpty(clientStr))
            throw new IllegalReceiveException(format(PARAM_IS_EMPTY_ERROR, CLIENT));
        try {
            Client client = objectMapper.readValue(clientStr, Client.class);
            return clientDao.addClient(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> getPaymentTypes() {
        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add(configuration.getPaymentTypeName(CASH));
        paymentTypes.add(configuration.getPaymentTypeName(CREDIT_CARD));
        paymentTypes.add(configuration.getPaymentTypeName(CREDIT));
        paymentTypes.add(configuration.getPaymentTypeName(BARTER));
        paymentTypes.add(configuration.getPaymentTypeName(OFFSET));
        return paymentTypes;
    }
}
