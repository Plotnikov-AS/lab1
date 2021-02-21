package ru.unvier.pis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.unvier.pis.model.FulfillResult;
import ru.unvier.pis.model.entity.Client;
import ru.unvier.pis.model.entity.Product;
import ru.unvier.pis.service.ClientService;
import ru.unvier.pis.service.StoreService;

import java.util.List;

import static ru.unvier.pis.constants.Constants.Url.ORDER;
import static ru.unvier.pis.constants.Constants.Url.SALE;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final ClientService clientService;
    private final StoreService storeService;

    @PostMapping(ORDER)
    public String order(@RequestParam("clientId") Long id,
                        @RequestParam("paymentType") String paymentType,
                        Model model) {
        Client client = clientService.getClient(id);
        List<Product> products = storeService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("client", client);
        model.addAttribute("paymentType", paymentType);
        return "order/order";
    }

    @PostMapping(SALE)
    public String sale(@RequestParam("paymentType") String paymentType,
                       @RequestParam("cart") String cart,
                       @RequestParam("clientId") Long clientId,
                       Model model) {
        FulfillResult result = storeService.fulfillOrder(paymentType, cart, clientId);
        model.addAttribute("cart", storeService.getSubmittedProducts(cart));
        model.addAttribute("result", result);
        return "order/fulfillResult";
    }
}
