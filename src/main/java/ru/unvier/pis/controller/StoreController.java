package ru.unvier.pis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.unvier.pis.configuration.ApplicationConfiguration;
import ru.unvier.pis.model.FulfillResult;
import ru.unvier.pis.model.PaymentType;
import ru.unvier.pis.model.entity.Client;
import ru.unvier.pis.model.entity.Product;
import ru.unvier.pis.service.ClientService;
import ru.unvier.pis.service.StoreService;

import java.util.List;

import static ru.unvier.pis.constants.Constants.Url.*;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final ClientService clientService;
    private final StoreService storeService;
    private final ApplicationConfiguration configuration;

    @PostMapping(ORDER)
    public String order(@RequestParam("clientId") Long id,
                        @RequestParam("paymentType") String paymentType,
                        Model model) {
        if (PaymentType.BARTER.equals(configuration.parsePaymentType(paymentType))) {
            return "redirect:/barter?clientId=" + id + "&paymentType=" + configuration.parsePaymentType(paymentType);
        }

        Client client = clientService.getClient(id);
        List<Product> products = storeService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("client", client);
        model.addAttribute("paymentType", paymentType);
        return "order/order";
    }

    @GetMapping(BARTER)
    public String barter(@RequestParam("clientId") Long id,
                         @RequestParam("paymentType") String paymentType,
                         Model model) {
        Client client = clientService.getClient(id);
        List<Product> products = storeService.getAllProducts();

        model.addAttribute("paymentType", paymentType);
        model.addAttribute("products", products);
        model.addAttribute("client", client);
        return "order/barter";
    }

    @PostMapping(SALE)
    public String sale(@RequestParam("paymentType") String paymentType,
                       @RequestParam(value = "clientProductsJson", required = false) String clientProducts,
                       @RequestParam("cart") String cart,
                       @RequestParam("clientId") Long clientId,
                       Model model) {
        System.out.println("Cart: " + cart);
        System.out.println("Products: " + clientProducts);
        FulfillResult result = storeService.fulfillOrder(paymentType, cart, clientId, clientProducts);
        model.addAttribute("cart", storeService.getSubmittedProducts(cart));
        model.addAttribute("result", result);
        return "order/fulfillResult";
    }
}
