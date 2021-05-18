package ru.unvier.pis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unvier.pis.configuration.ApplicationConfiguration;
import ru.unvier.pis.dao.ClientDao;
import ru.unvier.pis.dao.FinAccountDao;
import ru.unvier.pis.dao.ProductDao;
import ru.unvier.pis.model.Cart;
import ru.unvier.pis.model.FulfillResult;
import ru.unvier.pis.model.PaymentType;
import ru.unvier.pis.model.entity.Client;
import ru.unvier.pis.model.entity.FinAccount;
import ru.unvier.pis.model.entity.Product;

import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.unvier.pis.constants.Constants.Config.FAULT;
import static ru.unvier.pis.constants.Constants.Config.SUCCESS;
import static ru.unvier.pis.constants.Constants.ErrorMessages.*;
import static ru.unvier.pis.constants.Constants.ParamNames.*;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final ApplicationConfiguration configuration;
    private final ProductDao productDao;
    private final ClientDao clientDao;
    private final FinAccountDao finAccountDao;
    private final ObjectMapper objectMapper;

    public List<Product> getAllProducts() {
        return productDao.getAllExistingProducts();
    }

    public List<Map<String, String>> getSubmittedProducts(String strCart) {
        List<Map<String, String>> products = new ArrayList<>();
        try {
            Cart cart = objectMapper.readValue(strCart, Cart.class);
            cart.getProducts().stream()
                    .filter(Objects::nonNull)
                    .forEach(prod -> {
                        Product product = productDao.getProductById(Long.valueOf(prod.get(PRODUCT_ID)));
                        Map<String, String> map = new HashMap<>();
                        map.put("productId", String.valueOf(product.getId()));
                        map.put(PRODUCT_NAME, product.getProductName());
                        map.put(COUNT, prod.get(COUNT));
                        map.put(PRICE_EACH, product.getProductPrice());
                        map.put(PRICE_TOTAL, String.valueOf(parseDouble(product.getProductPrice()) * parseInt(prod.get(COUNT))));
                        products.add(map);
                    });
            return products;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format(CANT_PARSE_OBJECT_ERROR, strCart));
        }
    }

    public void fulfillOrder(String paymentType, String strCart, Long clientId) {
        try {
            Cart cart = objectMapper.readValue(strCart, Cart.class);
            Double totalCartPrice = totalCartPrice(cart);
            Client client = clientDao.getClientById(clientId);
            FinAccount finAccount = client.getFinAccount();
            PaymentType pmntType = PaymentType.parsePaymentType(paymentType);
            if (isNull(pmntType)) {
                pmntType = configuration.parsePaymentType(paymentType);
            }
            switch (pmntType) {
                case CASH:
                    sellForCash(finAccount, totalCartPrice, cart);
                    break;
                case CREDIT_CARD:
                    sellForCreditCard(finAccount, totalCartPrice, cart);
                    break;
                case BACK_RETURN:
                    doReturn(cart, client);
                    break;
                default:
                    throw new RuntimeException(format(UNKNOWN_PAYMENT_TYPE, pmntType));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format(CANT_PARSE_OBJECT_ERROR, e.getMessage()));
        }
    }

    private void sellForCreditCard(FinAccount finAccount, Double totalCartPrice, Cart cart) {
        increaseTotalSpent(finAccount, totalCartPrice);
        decreaseProducts(cart);
    }

    private void sellForCash(FinAccount finAccount, Double totalCartPrice, Cart cart) {
        increaseTotalSpent(finAccount, totalCartPrice);
        decreaseProducts(cart);
    }

    public void doReturn(Cart cart, Client client) {
        increaseProducts(cart);
        decreaseTotalSpent(cart, client);
    }

    private void decreaseTotalSpent(Cart cart, Client client) {
        Double totalCartPrice = totalCartPrice(cart);
        FinAccount finAccount = client.getFinAccount();
        Double curTotalSpent = finAccount.getTotalSpent();
        finAccount.setTotalSpent(Math.max(0, curTotalSpent - totalCartPrice));
        finAccountDao.updateFinAccount(finAccount);
    }

    private void increaseProducts(Cart cart) {
        if (isNull(cart) || isEmpty(cart.getProducts()))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CART));

        for (Map<String, String> product : cart.getProducts()) {
            if (isNull(product)) continue;

            Long productId = parseLong(product.get(PRODUCT_ID));
            int count = parseInt(product.get(COUNT));
            Product pr = productDao.getProductById(productId);
            int newCount = parseInt(pr.getCountLeft()) + count;
            pr.setCountLeft(String.valueOf(newCount));
            productDao.updateProduct(pr);
        }
    }

    private void increaseTotalSpent(FinAccount finAccount, Double totalCartPrice) {
        Double curTotalSpent = finAccount.getTotalSpent();
        finAccount.setTotalSpent(curTotalSpent + totalCartPrice);
        finAccountDao.updateFinAccount(finAccount);
    }

    private void decreaseProducts(Cart cart) {
        if (isNull(cart) || isEmpty(cart.getProducts()))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CART));
        for (Map<String, String> product : cart.getProducts()) {
            if (isNull(product)) continue;

            Long productId = parseLong(product.get(PRODUCT_ID));
            int count = parseInt(product.get(COUNT));
            Product pr = productDao.getProductById(productId);
            int newCount = parseInt(pr.getCountLeft()) - count;
            if (newCount < 0)
                throw new RuntimeException(format(PARAM_LESS_THEN_ZERO_ERROR, COUNT));
            pr.setCountLeft(String.valueOf(newCount));
            productDao.updateProduct(pr);
        }
    }

    private Double totalCartPrice(Cart cart) {
        if (isNull(cart) || isEmpty(cart.getProducts()))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CART));

        double totalPrice = 0.0;
        for (Map<String, String> product : cart.getProducts()) {
            Long productId = parseLong(product.get(PRODUCT_ID));
            int count = parseInt(product.get(COUNT));
            Product pr = productDao.getProductById(productId);
            double price = count * parseDouble(pr.getProductPrice());
            totalPrice += price;
        }
        return totalPrice;
    }
}
