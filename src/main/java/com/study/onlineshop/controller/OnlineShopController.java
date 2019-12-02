package com.study.onlineshop.controller;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Maksym Kyrychenko
 * @since 01.12.2019
 */
@Controller
@RequestMapping("/")
public class OnlineShopController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listAllProducts(Model model) {
        model.addAttribute("products", productService.getAll());
        return "products";
    }

    @GetMapping(path = "product/edit/{productId}")
    public String editPage(@PathVariable int productId, Model model) {
        model.addAttribute("product", productService.getById(productId));
        return "edit";
    }

    @PostMapping(path = "product/edit/{productId}")
    public String editEntry(@PathVariable int productId, @RequestParam String name, @RequestParam double price) {
        productService.update(productId, name, price);
        return "redirect:/";
    }

    @GetMapping(path = "product/add")
    protected String addProductPage() {
        return "add";
    }

    @PostMapping(path = "product/add")
    protected String addProduct(@RequestParam String name, @RequestParam double price) {
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
