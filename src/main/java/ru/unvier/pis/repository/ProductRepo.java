package ru.unvier.pis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query("SELECT prod " +
            "FROM Product prod " +
            "WHERE prod.countLeft > 0")
    List<Product> getAllExisting();

    Product getById(Long id);

    @Query("SELECT DISTINCT prod " +
            "FROM Product prod " +
            "WHERE UPPER(prod.productName) like CONCAT('%', UPPER(:productName), '%')")
    List<Product> getProductsByProductName(@Param("productName") String productName);

    @Query("SELECT prod " +
            "FROM Product prod")
    List<Product> getAll();
}
