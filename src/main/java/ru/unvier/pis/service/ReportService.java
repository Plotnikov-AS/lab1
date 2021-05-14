package ru.unvier.pis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unvier.pis.dao.ProductDao;
import ru.unvier.pis.dao.ReportDao;
import ru.unvier.pis.model.Cart;
import ru.unvier.pis.model.PaymentType;
import ru.unvier.pis.model.Sale;
import ru.unvier.pis.model.entity.Product;
import ru.unvier.pis.model.entity.Report;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Comparator.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.unvier.pis.constants.Constants.ParamNames.COUNT;
import static ru.unvier.pis.constants.Constants.ParamNames.PRODUCT_ID;
import static ru.unvier.pis.model.PaymentType.*;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ProductDao productDao;
    private final ReportDao reportDao;
    private final ObjectMapper objectMapper;

    public Long saveReport(String strCart, String saleType) throws JsonProcessingException {
        Cart cart = objectMapper.readValue(strCart, Cart.class);
        List<Product> products = getProducts(cart);
        if (isEmpty(products)) {
            throw new RuntimeException("Empty products");
        }
        Long reportNumber = reportDao.getNextReportNumber();

        products.forEach(product -> {
            Report report = new Report();
            report.setSaleType(saleType.toUpperCase());
            report.setSaleDate(new Date(System.currentTimeMillis()));
            Product productFromDb = productDao.getProductById(product.getId());
            report.setProduct(productFromDb);
            int count = 0;
            for (Map<String, String> prod : cart.getProducts()) {
                if (prod.get(PRODUCT_ID).equals(product.getId().toString())) {
                    count = Integer.parseInt(prod.get(COUNT));
                    break;
                }
            }
            report.setCount(count);
            report.setReportNumber(reportNumber);
            reportDao.saveReport(report);
        });
        return reportNumber;
    }

    private List<Product> getProducts(Cart cart) {
        if (isNull(cart) || isNull(cart.getProducts()))
            return emptyList();

        List<Product> products = new ArrayList<>();
        cart.getProducts().forEach(product -> {
            Product prod = productDao.getProductById(Long.valueOf(product.get(PRODUCT_ID)));
            products.add(prod);
        });

        return products;
    }

    public List<Report> getReportsByReportNumber(Long reportNumber) {
        return reportDao.getReportByReportNumber(reportNumber);
    }

    public List<Product> getProductsInReport(Long reportNumber) {
        List<Report> reports = reportDao.getReportByReportNumber(reportNumber);
        List<Product> products = new ArrayList<>();
        reports.forEach(rep -> products.add(rep.getProduct()));

        return products;
    }

    public List<Product> getProductsInReport(Long reportNumber, String sellDate) {
        List<Report> report = reportDao.getReportByNumberAndDate(reportNumber, sellDate);
        List<Product> products = new ArrayList<>();
        report.forEach(rep -> products.add(productDao.getProductById(rep.getProductId())));

        return products;
    }

    public Map<String, Map<String, List<Sale>>> getSales(String dateFrom, String dateTo, String paymentType) {
        List<Report> reports = reportDao.getReports(dateFrom, dateTo, parsePaymentType(paymentType).engName.toUpperCase());
        Map<Long, Sale> sales = new HashMap<>();
        reports.forEach(report -> {
            Sale sale = sales.get(report.getReportNumber());
            if (isNull(sale)) {
                Double amount = Double.parseDouble(report.getProduct().getProductPrice()) * report.getCount();
                sale = Sale.builder()
                        .date(report.getSaleDate())
                        .number(report.getReportNumber())
                        .type(report.getSaleType())
                        .amount(amount)
                        .build();
                sales.put(report.getReportNumber(), sale);
            } else {
                Double curAmount = sale.getAmount();
                Double amount = Double.parseDouble(report.getProduct().getProductPrice()) * report.getCount();
                Double newAmount = curAmount + amount;
                sale.setAmount(newAmount);
            }
        });

        List<Sale> cashSales = CASH.equals(paymentType) || ALL.equals(paymentType) ? getSalesByType(sales, CASH) : emptyList();
        List<Sale> creditCardSales = CREDIT_CARD.equals(paymentType) || ALL.equals(paymentType) ? getSalesByType(sales, CREDIT_CARD) : emptyList();
        List<Sale> backReturns = BACK_RETURN.equals(paymentType) || ALL.equals(paymentType) ? getSalesByType(sales, BACK_RETURN) : emptyList();

        Map<String, List<Sale>> cashSalesByDate = getSalesByDates(cashSales);
        Map<String, List<Sale>> creditCardSalesByDate = getSalesByDates(creditCardSales);
        Map<String, List<Sale>> backReturnsByDate = getSalesByDates(backReturns);

        Map<String, Map<String, List<Sale>>> result = new HashMap<>();
        result.put(CASH.engName, cashSalesByDate);
        result.put(CREDIT_CARD.engName, creditCardSalesByDate);
        result.put(BACK_RETURN.engName, backReturnsByDate);

        return result;
    }

    private Map<String, List<Sale>> getSalesByDates(List<Sale> sales) {
        if (isEmpty(sales)) {
            return emptyMap();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Map<String, List<Sale>> salesByDates = new HashMap<>();
        sales.sort(comparing(Sale::getDate));
        List<Sale> firstSaleByDate = new ArrayList<>();
        firstSaleByDate.add(sales.get(0));
        salesByDates.put(sdf.format(sales.get(0).getDate()), firstSaleByDate);
        for (int i = 1; i < sales.size(); i++) {
            Sale sale = sales.get(i);
            Sale prevSale = sales.get(i - 1);
            String saleDate = sdf.format(sale.getDate());
            if (nonNull(prevSale)) {
                String prevSaleDate = sdf.format(prevSale.getDate());
                if (saleDate.equals(prevSaleDate)) {
                    salesByDates
                            .get(saleDate)
                            .add(sale);
                } else {
                    List<Sale> salesByNewDate = new ArrayList<>();
                    salesByNewDate.add(sale);
                    salesByDates.put(saleDate, salesByNewDate);
                }
            }
        }
        return salesByDates;
    }

    private List<Sale> getSalesByType(Map<Long, Sale> sales, PaymentType type) {
        List<Sale> salesByType = new ArrayList<>();
        sales.forEach((saleNum, sale) -> {
            if (type.equals(sale.getType())) {
                salesByType.add(sale);
            }
        });

        return salesByType;
    }

    public Double getTotalByType(Map<String, Map<String, List<Sale>>> sales, PaymentType paymentType) {
        Map<String, List<Sale>> salesByType = sales.get(paymentType.engName);
        Double totalAmount = 0d;
        for (Map.Entry<String, List<Sale>> entry : salesByType.entrySet()) {
            List<Sale> salesList = entry.getValue();
            for (Sale sale : salesList) {
                totalAmount += sale.getAmount();
            }
        }
        return totalAmount;
    }

    public Map<String, Map<String, Double>> getTotalByDays(Map<String, Map<String, List<Sale>>> sales) {
        Map<String, Map<String, Double>> totalByDays = new HashMap<>();
        sales.forEach((saleType, salesByDates) -> {
            Map<String, Double> date2amount = new HashMap<>();
            for (Map.Entry<String, List<Sale>> entry : salesByDates.entrySet()) {
                String date = entry.getKey();
                List<Sale> sale = entry.getValue();
                Double amount = 0d;
                for (Sale saleInDate : sale) {
                    amount += saleInDate.getAmount();
                }
                date2amount.put(date, amount);
            }
            totalByDays.put(saleType, date2amount);
        });
        return totalByDays;
    }
}
