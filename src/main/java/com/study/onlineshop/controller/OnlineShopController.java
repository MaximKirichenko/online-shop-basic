package com.study.onlineshop.controller;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Maksym Kyrychenko
 * @since 01.12.2019
 */
@Controller
@RequestMapping("/")
public class OnlineShopController {

    @Autowired
    public ProductService productService;
    @Autowired
    public PageGenerator pageGenerator;

    @ResponseBody
    @GetMapping
    public String listAllProducts() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", productService.getAll());
        return pageGenerator.getPage("products", parameters);
    }

    @ResponseBody
    @GetMapping(path = "product/edit/{productId}")
    public String editPage(@PathVariable int productId) {
        Product product = productService.getById(productId);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("product", product);
        return pageGenerator.getPage("edit", parameters);
    }

    @PostMapping(path = "product/edit/{productId}")
    public String editEntry(@PathVariable int productId, @RequestParam String name, @RequestParam double price) {
        productService.update(productId, name, price);
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping(path = "product/add")
    protected String addProductPage() {
        return pageGenerator.getPage("add", new HashMap<>());
    }

    @PostMapping(path = "product/add")
    protected String addProduct(@RequestParam String name, @RequestParam double price) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        int id = productService.add(product);
        product.setId(id);
        return "redirect:/";
    }

    @PostMapping(path = "product/delete/{productId}")
    public String delete(@PathVariable int productId) {
        productService.delete(productId);
        return "redirect:/";
    }
}
