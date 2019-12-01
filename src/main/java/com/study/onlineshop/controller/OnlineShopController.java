package com.study.onlineshop.controller;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Maksym Kyrychenko
 * @since 01.12.2019
 */
@Controller
@RequestMapping("/")
public class OnlineShopController {
    @Autowired
    public ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    public void listAllProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        List<Product> products = productService.getAll();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);

        String page = pageGenerator.getPage("products", parameters);
        response.getWriter().write(page);
    }

    @RequestMapping(path = "/product/edit/", method = RequestMethod.GET)
    public void editPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = getId(request.getRequestURI());
        PageGenerator pageGenerator = PageGenerator.instance();
        Product product = productService.getById(id);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("product", product);

        String page = pageGenerator.getPage("edit", parameters);
        response.getWriter().write(page);
    }

    @RequestMapping(path = "/product/edit/", method = RequestMethod.POST)
    public void editEntry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        double price = Double.valueOf(request.getParameter("price"));
        if (name == null || name.isEmpty() || price <= 0d) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            int id = getId(request.getRequestURI());
            productService.update(id, name, price);
            response.sendRedirect("/products");
        }
    }

    @RequestMapping(path = "/products/add", method = RequestMethod.GET)
    protected void addProductPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();

        HashMap<String, Object> parameters = new HashMap<>();

        String page = pageGenerator.getPage("add", parameters);
        response.getWriter().write(page);
    }

    @RequestMapping(path = "/products/add", method = RequestMethod.POST)
    protected void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        double price = Double.valueOf(request.getParameter("price"));
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        int id = productService.add(product);
        product.setId(id);
        response.sendRedirect("/products");
    }

    @RequestMapping(path = "/product/delete/", method = RequestMethod.POST)
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        int index = uri.lastIndexOf("/");
        int id = Integer.valueOf(uri.substring(index + 1, uri.length()));
        productService.delete(id);
        resp.sendRedirect("/products");
    }

    private int getId(String uri) {
        int index = uri.lastIndexOf("/");
        int id = Integer.valueOf(uri.substring(index + 1, uri.length()));
        return id;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
