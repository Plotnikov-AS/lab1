package ru.unvier.pis.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.Product;
import ru.unvier.pis.repository.ProductRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.unvier.pis.constants.Constants.ErrorMessages.*;
import static ru.unvier.pis.constants.Constants.ParamNames.*;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProductDao {
    private final ProductRepo productRepo;
    @PersistenceContext
    private final EntityManager entityManager;

    public List<Product> getAllExistingProducts() {
        return productRepo.getAllExisting();
    }

    public List<Product> getAllProducts() {
        return productRepo.getAll();
    }

    public Product getProductById(Long id) {
        if(isNull(id))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, ID));

        return productRepo.getById(id);
    }

    public List<Product> getProductsByName(String productName) {
        if(isNull(productName))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, PRODUCT_NAME));

        return productRepo.getProductsByProductName(productName);
    }

    public void updateProduct(Product product) {
        if(isNull(product))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, PRODUCT));

        List<Product> existingProducts = productRepo.getProductsByProductName(product.getProductName());
        if (isEmpty(existingProducts))
            throw new RuntimeException(format(NOTHING_WAS_FOUND_ERROR, PRODUCT_NAME, product.getProductName()));
        if (existingProducts.size() > 1)
            throw new RuntimeException(format(NON_UNIQUE_RESULT_ERROR, PRODUCT_NAME, product.getProductName()));
        Product existingProduct = existingProducts.get(0);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductPrice(product.getProductPrice());
        existingProduct.setCountLeft(product.getCountLeft());

        entityManager.persist(existingProduct);
    }

    public void addNewProduct(Product product) {
        if(isNull(product))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, PRODUCT));

        List<Product> existingProduct = productRepo.getProductsByProductName(product.getProductName());
        if (nonNull(existingProduct) && existingProduct.size() > 0)
            return;

        entityManager.persist(product);
    }
}
