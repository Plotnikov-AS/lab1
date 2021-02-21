package ru.unvier.pis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.Client;

import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    Client getClientById(Long id);

    List<Client> getClientsByLastName(String lastName);

    List<Client> getClientsByFirstName(String firstName);

    List<Client> getClientsByMiddleName(String middleName);

    List<Client> getClientsByOrganizationName(String organizationName);

    @Query("SELECT DISTINCT client " +
            "FROM Client client " +
            "WHERE client.lastName = :lastName " +
            "AND client.firstName = :firstName " +
            "AND client.middleName = :middleName " +
            "AND client.organizationName = :organizationName")
    Client getByFioAndOrganization(@Param("lastName") String lastName,
                                   @Param("firstName") String firstName,
                                   @Param("middleName") String middleName,
                                   @Param("organizationName") String organizationName);
}
