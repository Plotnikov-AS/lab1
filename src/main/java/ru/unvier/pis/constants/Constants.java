package ru.unvier.pis.constants;

public interface Constants {
    Double DEFAULT_CREDIT_AMOUNT = 100.0;

    interface Url {
        String GET_CLIENT = "getClient";
        String SEARCH_CLIENT = "searchClient";
        String CREATE_CLIENT = "createClient";
        String ADD_CLIENT = "addClient";
        String DELETE_CLIENT = "deleteClient";
        String ORDER = "order";
        String SALE = "sale";
        String BARTER = "barter";
        String PRODUCTS = "products";
        String ADD_PRODUCT = "addProduct";
        String GET_ALL_PRODUCTS = "getAllProducts";
        String CHANGE_PRODUCT = "changeProduct";
        String GET_PRODUCT = "getProduct";
        String SEARCH_PRODUCT = "searchProduct";
    }

    interface Config {
        String APPLICATION_CONFIG_PREFIX = "application";
        String SUCCESS = "success";
        String FAULT = "fault";
    }

    interface ErrorMessages {
        String PARAM_IS_EMPTY_ERROR = "%s is empty!";
        String ALL_PARAMS_ARE_EMPTY_ERROR = "All params are empty!";
        String NOTHING_WAS_FOUND_ERROR = "Noting was found for %s = '%s'";
        String PARAM_LESS_THEN_ZERO_ERROR = "%s less then zero!";
        String NEW_VALUE_MORE_THAN_MAX_POSSIBLE_ERROR = "New %s = '%s' more than max possible %s = '%s'";
        String UNKNOWN_PAYMENT_TYPE = "Unknown payment type: %s";
        String CANT_PARSE_OBJECT_ERROR = "Can't parse: %s";
        String NON_UNIQUE_RESULT_ERROR = "Non unique result for %s = %s";
    }

    interface ParamNames {
        String ID = "id";
        String CLIENT_FIRST_NAME = "clientFirstName";
        String CLIENT_LAST_NAME = "clientFirstName";
        String CLIENT_MIDDLE_NAME = "clientFirstName";
        String ORGANIZATION_NAME = "clientFirstName";
        String CLIENT = "client";
        String CART = "cart";
        String PRODUCT = "product";
        String PRODUCT_NAME = "productName";
        String FIN_ACCOUNT = "finAccount";
        String COUNT = "count";
        String BALANCE = "balance";
        String DEBT = "debt";
        String CREDIT_LEFT = "creditLeft";
        String PAYMENT_TYPE = "paymentType";
        String PRODUCT_ID = "productId";
        String PRICE_EACH = "priceEach";
        String PRICE_TOTAL = "priceTotal";
    }
}
