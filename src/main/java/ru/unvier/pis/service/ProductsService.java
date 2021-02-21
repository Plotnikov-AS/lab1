package ru.unvier.pis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unvier.pis.dao.ProductDao;
import ru.unvier.pis.model.entity.Product;

import java.util.List;

import static java.lang.String.format;
import static ru.unvier.pis.constants.Constants.ErrorMessages.CANT_PARSE_OBJECT_ERROR;
import static ru.unvier.pis.constants.Constants.ParamNames.PRODUCT;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductDao productDao;
    private final ObjectMapper objectMapper;

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public Product addProduct(String productStr) {
        try {
            Product product = objectMapper.readValue(productStr, Product.class);
            productDao.addNewProduct(product);
            return product;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format(CANT_PARSE_OBJECT_ERROR, PRODUCT, productStr));
        }
    }

    public Product changeProduct(String productStr) {
        try {
            Product product = objectMapper.readValue(productStr, Product.class);
            productDao.updateProduct(product);
            return product;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format(CANT_PARSE_OBJECT_ERROR, PRODUCT, productStr));
        }
    }

    public List<Product> getProducts(String productStr) {
        try {
            Product product = objectMapper.readValue(productStr, Product.class);
            return productDao.getProductsByName(product.getProductName());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format(CANT_PARSE_OBJECT_ERROR, PRODUCT, productStr));
        }
    }
}
