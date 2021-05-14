package ru.unvier.pis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.unvier.pis.configuration.ApplicationConfiguration;
import ru.unvier.pis.model.PaymentType;
import ru.unvier.pis.model.entity.Client;
import ru.unvier.pis.model.entity.Product;
import ru.unvier.pis.model.entity.Report;
import ru.unvier.pis.service.ClientService;
import ru.unvier.pis.service.ReportService;
import ru.unvier.pis.service.StoreService;

import java.util.Date;
import java.util.List;

import static ru.unvier.pis.constants.Constants.Url.*;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final ClientService clientService;
    private final StoreService storeService;
    private final ReportService reportService;
    private final ApplicationConfiguration configuration;

    @PostMapping(ORDER)
    public String order(@RequestParam("clientId") Long id,
                        @RequestParam("paymentType") String paymentType,
                        Model model) {
        if (PaymentType.BACK_RETURN.equals(configuration.parsePaymentType(paymentType))) {
            model.addAttribute("client", clientService.getClient(id));

            return "order/backReturn";
        }

        Client client = clientService.getClient(id);
        List<Product> products = storeService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("client", client);
        model.addAttribute("paymentType", paymentType);
        return "order/order";
    }

    @PostMapping(BACK_RETURN)
    public String backReturn(Long clientId) {
        return null; //todo
    }

    @PostMapping(SALE)
    public String sale(@RequestParam("paymentType") String paymentType,
                       @RequestParam("cart") String cart,
                       @RequestParam("clientId") Long clientId,
                       Model model) throws JsonProcessingException {
        PaymentType pmtType = configuration.parsePaymentType(paymentType);
        switch (pmtType) {
            case CREDIT_CARD:
                model.addAttribute("cart", storeService.getSubmittedProducts(cart));
                model.addAttribute("client", clientService.getClient(clientId));

                return "order/bill";
            case CASH:
                Long reportNumber = reportService.saveReport(cart, "cash");
                model.addAttribute("reportNumber", reportNumber);
                model.addAttribute("cassaNumber", "123");
                model.addAttribute("cart", storeService.getSubmittedProducts(cart));
                model.addAttribute("client", clientService.getClient(clientId));

                return "sell/recipe";
            case BACK_RETURN:

            default:
                throw new RuntimeException("Payment type not found");
        }
    }

    @PostMapping("/creditPayed")
    public String payed(String cart, Long clientId, Model model) throws JsonProcessingException {
        storeService.fulfillOrder("Безналичный", cart, clientId);
        Long reportNumber = reportService.saveReport(cart, "creditCard");

        model.addAttribute("reportNumber", reportNumber);
        model.addAttribute("cart", storeService.getSubmittedProducts(cart));
        model.addAttribute("client", clientService.getClient(clientId));
        return "sell/creditCardSell";
    }

    @PostMapping("/cashPayed")
    public String cashPayed(String cart, Long clientId, Model model) throws JsonProcessingException {
        storeService.fulfillOrder("Наличный", cart, clientId);

        return "redirect:/";
    }

    @PostMapping("/getProductsInReport")
    public String getProductsInReport(Long reportNumber, String sellDate, Long clientId, Model model) {
        List<Report> report = reportService.getReportsByReportNumber(reportNumber);
        List<Product> products = reportService.getProductsInReport(reportNumber, sellDate);
        model.addAttribute("products", products);
        model.addAttribute("report", report);
        model.addAttribute("client", clientService.getClient(clientId));

        return "order/backReturnWithProducts";
    }

    @PostMapping("/doReturn")
    public String doReturn(String cart, Long clientId, Model model) throws JsonProcessingException {
        storeService.fulfillOrder("Возврат", cart, clientId);
        Long reportNumber = reportService.saveReport(cart, "backReturn");
        List<Product> products = reportService.getProductsInReport(reportNumber);
        List<Report> report = reportService.getReportsByReportNumber(reportNumber);
        model.addAttribute("reportNumber", reportNumber);
        model.addAttribute("products", products);
        model.addAttribute("report", report);
        model.addAttribute("client", clientService.getClient(clientId));

        return "sell/return";
    }
}
