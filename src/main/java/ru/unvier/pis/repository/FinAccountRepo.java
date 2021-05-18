package ru.unvier.pis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.Client;
import ru.unvier.pis.model.entity.FinAccount;

@Repository
public interface FinAccountRepo  extends JpaRepository<FinAccount, Long> {
    FinAccount getById(Long id);
}
