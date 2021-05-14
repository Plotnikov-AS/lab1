package ru.unvier.pis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.unvier.pis.model.Sale;
import ru.unvier.pis.service.ReportService;

import java.util.List;
import java.util.Map;

import static ru.unvier.pis.model.PaymentType.*;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/saveReport")
    public String saveReport(String cart, Long clientId, String saleType) throws JsonProcessingException {
        reportService.saveReport(cart, saleType);

        return "redirect:/";
    }

    @GetMapping("/searchReports")
    public String searchReports() {
        return "report/search";
    }

    @PostMapping("/findReports")
    public String findReports(String dateFrom, String dateTo, String paymentType, Model model) {
        Map<String, Map<String, List<Sale>>> sales = reportService.getSales(dateFrom, dateTo, paymentType);
        Double totalCash = reportService.getTotalByType(sales, CASH);
        Double totalCredit = reportService.getTotalByType(sales, CREDIT_CARD);
        Double totalReturns = reportService.getTotalByType(sales, BACK_RETURN);
        Map<String, Map<String, Double>> totalByDays = reportService.getTotalByDays(sales);

        model.addAttribute("totalByDays", totalByDays);
        model.addAttribute("sales", sales);
        model.addAttribute("totalCash", totalCash);
        model.addAttribute("totalCredit", totalCredit);
        model.addAttribute("totalReturns", totalReturns);

        return "report/reports";
    }
}
