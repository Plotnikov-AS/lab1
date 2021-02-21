package ru.unvier.pis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.unvier.pis.model.entity.Product;
import ru.unvier.pis.service.ProductsService;

import java.util.List;

import static ru.unvier.pis.constants.Constants.Url.*;

@Controller
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping(PRODUCTS)
    public String products() {
        return "product/productMainPage";
    }

    @GetMapping(GET_ALL_PRODUCTS)
    public String getAllProducts(Model model) {
        List<Product> allProducts = productsService.getAllProducts();
        model.addAttribute("products", allProducts);
        return "product/allProducts";
    }

    @GetMapping(ADD_PRODUCT)
    public String addProductPage() {
        return "product/addNewProduct";
    }

    @GetMapping(SEARCH_PRODUCT)
    public String searchProductPage() {
        return "product/searchProduct";
    }

    @GetMapping(CHANGE_PRODUCT)
    public String getChangeProductPage(@RequestParam("product") String productStr,
                                       Model model) {
        Product product = productsService.changeProduct(productStr);
        model.addAttribute("product", product);
        return "product/changeProductPage";
    }

    @PostMapping(ADD_PRODUCT)
    public String addProducts(@RequestParam("product") String productStr,
                              Model model) {
        Product product = productsService.addProduct(productStr);
        model.addAttribute("product", product);
        return "product/newProductPage";
    }

    @PostMapping(CHANGE_PRODUCT)
    public String changeProduct(@RequestParam("product") String productStr,
                                Model model) {
        Product product = productsService.changeProduct(productStr);
        model.addAttribute("product", product);
        return "product/productPage";
    }

    @PostMapping(GET_PRODUCT)
    public String getProduct(@RequestParam("product") String productStr,
                             Model model) {
        List<Product> products = productsService.getProducts(productStr);
        if (products.size() == 1) {
            model.addAttribute("product", products.get(0));
            return "product/productPage";
        } else {
            model.addAttribute("products", products);
            return "product/allProducts";
        }
    }


}
