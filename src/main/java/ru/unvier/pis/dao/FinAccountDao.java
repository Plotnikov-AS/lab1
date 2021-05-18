package ru.unvier.pis.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.FinAccount;
import ru.unvier.pis.repository.FinAccountRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static ru.unvier.pis.constants.Constants.DEFAULT_CREDIT_AMOUNT;
import static ru.unvier.pis.constants.Constants.ErrorMessages.PARAM_IS_EMPTY_ERROR;
import static ru.unvier.pis.constants.Constants.ParamNames.FIN_ACCOUNT;

@Repository
@Transactional
@RequiredArgsConstructor
public class FinAccountDao {
    private final FinAccountRepo finAccountRepo;
    @PersistenceContext
    private final EntityManager entityManager;

    public FinAccount addFinAccount() {
        FinAccount finAccount = new FinAccount();
        finAccount.setTotalSpent(0.0);
        entityManager.persist(finAccount);

        return finAccount;
    }

    public void updateFinAccount(FinAccount finAccount) {
        if (isNull(finAccount))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, FIN_ACCOUNT));

        FinAccount existingFinAccount = finAccountRepo.getById(finAccount.getId());
        existingFinAccount.setTotalSpent(finAccount.getTotalSpent());

        entityManager.persist(finAccount);
    }
}
