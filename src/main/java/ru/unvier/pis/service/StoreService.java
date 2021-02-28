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

    public FulfillResult fulfillOrder(String paymentType, String strCart, Long clientId, String clientBarterProducts) {
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
                    return sellForCash(finAccount, totalCartPrice, cart);
                case CREDIT_CARD:
                    return sellForCreditCard(finAccount, totalCartPrice, cart);
                case CREDIT:
                    return sellForCredit(finAccount, totalCartPrice, cart);
                case BARTER:
                    return sellForBarter(cart, clientBarterProducts);
                case OFFSET:
                    return sellForOffset(finAccount, totalCartPrice, cart);
                default:
                    throw new RuntimeException(format(UNKNOWN_PAYMENT_TYPE, pmntType));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format(CANT_PARSE_OBJECT_ERROR, e.getMessage()));
        }
    }

    private FulfillResult sellForOffset(FinAccount finAccount, Double totalCartPrice, Cart cart) {
        try {
            decreaseClientDebt(finAccount, totalCartPrice);
            increaseCreditLeft(finAccount, totalCartPrice);
            increaseProducts(cart);
        } catch (RuntimeException e) {
            return FulfillResult.builder()
                    .status(configuration.getResult().get(FAULT))
                    .build();
        }
        return FulfillResult.builder().status(configuration.getResult().get(FAULT)).build();
    }




    private FulfillResult sellForBarter(Cart cart, String clientBarterProducts) throws JsonProcessingException {
        try {
            Cart clientProducts = objectMapper.readValue(clientBarterProducts, Cart.class);
            increaseProducts(clientProducts);
            decreaseProducts(cart);
            return FulfillResult.builder().
                    status(configuration.getResult().get(SUCCESS))
                    .build();
        } catch (RuntimeException e) {
            return FulfillResult.builder()
                    .status(configuration.getResult().get(FAULT))
                    .build();
        }
    }

    private FulfillResult sellForCredit(FinAccount finAccount, Double totalCartPrice, Cart cart) {
        try {
            decreaseClientBalance(finAccount, totalCartPrice);
            increaseTotalSpent(finAccount, totalCartPrice);
            decreaseProducts(cart);
            return FulfillResult.builder().status(configuration.getResult().get(SUCCESS)).build();
        } catch (RuntimeException e) {
            return FulfillResult.builder()
                    .status(configuration.getResult().get(FAULT))
                    .message(e.getMessage())
                    .build();
        }
    }

    private FulfillResult sellForCreditCard(FinAccount finAccount, Double totalCartPrice, Cart cart) {
        try {
            decreaseClientBalance(finAccount, totalCartPrice);
            increaseTotalSpent(finAccount, totalCartPrice);
            decreaseProducts(cart);
            return FulfillResult.builder().status(configuration.getResult().get(SUCCESS)).build();
        } catch (RuntimeException e) {
            return FulfillResult.builder()
                    .status(configuration.getResult().get(FAULT))
                    .message(e.getMessage())
                    .build();
        }
    }

    private FulfillResult sellForCash(FinAccount finAccount, Double totalCartPrice, Cart cart) {
        try {
            increaseTotalSpent(finAccount, totalCartPrice);
            decreaseProducts(cart);
            return FulfillResult.builder().status(configuration.getResult().get(SUCCESS)).build();
        } catch (RuntimeException e) {
            return FulfillResult.builder()
                    .status(configuration.getResult().get(FAULT))
                    .message(e.getMessage())
                    .build();
        }
    }

    private void increaseCreditLeft(FinAccount finAccount, Double totalCartPrice) {
        Double curCreditLeft = finAccount.getCreditLeft();
        Double maxCredit = finAccount.getCreditMax();
        double newCredidLeft = Double.min(maxCredit, (curCreditLeft + totalCartPrice));
        finAccount.setCreditLeft(newCredidLeft);
        finAccountDao.updateFinAccount(finAccount);
    }

    private void decreaseClientDebt(FinAccount finAccount, Double decreaseAmount) {
        Double curDebt = finAccount.getCurDebt();
        double newDebt = Double.max(0, (curDebt - decreaseAmount));
        finAccount.setCurDebt(newDebt);
        finAccountDao.updateFinAccount(finAccount);
    }

    private void increaseClientDebt(FinAccount finAccount, Double creditAmount) throws RuntimeException {
        Double curDebt = finAccount.getCurDebt();
        Double maxCredit = finAccount.getCreditMax();
        Double creditLeft = finAccount.getCreditLeft();
        double newDebt = curDebt + creditAmount;
        if (newDebt > maxCredit)
            throw new RuntimeException(format(NEW_VALUE_MORE_THAN_MAX_POSSIBLE_ERROR, DEBT, newDebt, CREDIT_LEFT, creditLeft));
        double newCreditLeft = Double.max(0, creditLeft - newDebt);
        finAccount.setCurDebt(newDebt);
        finAccount.setCreditLeft(newCreditLeft);
        finAccountDao.updateFinAccount(finAccount);
    }

    private void decreaseClientBalance(FinAccount finAccount, Double totalCartPrice) throws RuntimeException {
        Double curBalance = finAccount.getCurBalance();
        double newBalance = curBalance - totalCartPrice;
        if (newBalance < 0) {
            double creditAmount = totalCartPrice - curBalance;
            increaseClientDebt(finAccount, creditAmount);
            newBalance = 0.0;
        }
        finAccount.setCurBalance(newBalance);
        finAccountDao.updateFinAccount(finAccount);
    }

    private void increaseTotalSpent(FinAccount finAccount, Double totalCartPrice) {
        Double curTotalSpent = finAccount.getTotalSpent();
        finAccount.setTotalSpent(curTotalSpent + totalCartPrice);
        finAccountDao.updateFinAccount(finAccount);
    }

    private void increaseProducts(Cart cart) {
        if (isNull(cart) || isEmpty(cart.getProducts()))
            throw new IllegalArgumentException(format(PARAM_IS_EMPTY_ERROR, CART));
        for (Map<String, String> product : cart.getProducts()) {
            if (isNull(product)) continue;

            int count = parseInt(product.get(COUNT));
            if (count < 0)
                throw new RuntimeException(format(PARAM_LESS_THEN_ZERO_ERROR, COUNT));

            String productName = product.get(PRODUCT_NAME);
            Product newProduct = Product.builder()
                    .productName(productName)
                    .countLeft(String.valueOf(count))
                    .build();
            productDao.addNewProduct(newProduct);
        }
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
