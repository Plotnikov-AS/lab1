package ru.unvier.pis.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.Client;
import ru.unvier.pis.model.entity.FinAccount;
import ru.unvier.pis.repository.ClientRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.unvier.pis.constants.Constants.ErrorMessages.NOTHING_WAS_FOUND_ERROR;
import static ru.unvier.pis.constants.Constants.ErrorMessages.PARAM_IS_EMPTY_ERROR;
import static ru.unvier.pis.constants.Constants.ParamNames.*;

@Repository
@Transactional
@RequiredArgsConstructor
public class ClientDao {
    private final ClientRepo clientRepo;
    private final FinAccountDao finAccountDao;
    @PersistenceContext
    private final EntityManager entityManager;

    public Client getClientById(Long id) {
        if (isNull(id))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, ID));

        return clientRepo.getClientById(id);
    }

    public List<Client> getClientsByLastName(String lastName) {
        if (isNull(lastName))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CLIENT_LAST_NAME));

        return clientRepo.getClientsByLastName(lastName);
    }

    public List<Client> getClientsByFirstName(String firstName) {
        if (isNull(firstName))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CLIENT_FIRST_NAME));

        return clientRepo.getClientsByFirstName(firstName);
    }

    public List<Client> getClientsByMiddleName(String middleName) {
        if (isNull(middleName))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CLIENT_MIDDLE_NAME));

        return clientRepo.getClientsByMiddleName(middleName);
    }

    public List<Client> getClientsByOrganizationName(String organizationName) {
        if (isNull(organizationName))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, ORGANIZATION_NAME));

        return clientRepo.getClientsByOrganizationName(organizationName);
    }

    public Client addClient(Client client) {
        FinAccount finAccount = finAccountDao.addFinAccount();
        client.setFinAccount(finAccount);
        entityManager.persist(client);

        return client;
    }

    public Client updateClient(Client client) {
        if(isNull(client))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CLIENT));

        Client existingClient = clientRepo.getByFioAndOrganization(client.getLastName(), client.getFirstName(), client.getMiddleName(), client.getOrganizationName());
        if (isEmpty(existingClient))
            throw new RuntimeException(format(NOTHING_WAS_FOUND_ERROR, CLIENT, client.toString()));
        existingClient.setLastName(client.getLastName());
        existingClient.setFirstName(client.getFirstName());
        existingClient.setMiddleName(client.getMiddleName());
        existingClient.setOrganizationName(client.getOrganizationName());
        existingClient.setComment(client.getComment());

        entityManager.persist(client);

        return existingClient;
    }

}
