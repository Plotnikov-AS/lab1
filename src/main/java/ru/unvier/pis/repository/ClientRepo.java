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

    @Query("SELECT client " +
            "FROM Client client " +
            "WHERE UPPER(client.lastName) like CONCAT('%', UPPER(:lastName), '%')")
    List<Client> getClientsByLastName(@Param("lastName") String lastName);

    @Query("SELECT client " +
            "FROM Client client " +
            "WHERE UPPER(client.firstName) like CONCAT('%', UPPER(:firstName), '%')")
    List<Client> getClientsByFirstName(@Param("firstName") String firstName);

    @Query("SELECT client " +
            "FROM Client client " +
            "WHERE UPPER(client.middleName) like CONCAT('%', UPPER(:middleName), '%')")
    List<Client> getClientsByMiddleName(@Param("middleName") String middleName);

    @Query("SELECT client " +
            "FROM Client client " +
            "WHERE UPPER(client.organizationName) like CONCAT('%', UPPER(:organizationName), '%')")
    List<Client> getClientsByOrganizationName(@Param("organizationName") String organizationName);

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
