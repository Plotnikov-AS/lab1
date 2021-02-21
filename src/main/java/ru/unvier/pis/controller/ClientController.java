package ru.unvier.pis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.unvier.pis.model.entity.Client;
import ru.unvier.pis.service.ClientService;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.unvier.pis.constants.Constants.Url.*;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping(SEARCH_CLIENT)
    public String clientsPage() {
        return "client/searchClient";
    }

    @PostMapping(GET_CLIENT)
    public String getClient(@RequestParam(value = "client") String clientStr,
                            Model model) {

        List<Client> clients = clientService.getClients(clientStr);
        if (isEmpty(clients) || clients.size() > 1) {
            model.addAttribute("clients", clients);
            return "client/clients";
        } else {
            Client client = clients.get(0);
            model.addAttribute("client", client);
            model.addAttribute("paymentTypes", clientService.getPaymentTypes());
            return "client/client";
        }
    }

    @GetMapping(CREATE_CLIENT)
    public String createClient() {
        return "client/newClient";
    }

    @PostMapping(ADD_CLIENT)
    public String addClient(@RequestParam(value = "client") String clientStr,
                            Model model) {
        Client client = clientService.addClient(clientStr);
        model.addAttribute("client", client);
        model.addAttribute("paymentTypes", clientService.getPaymentTypes());
        return "client/client";
    }
}
