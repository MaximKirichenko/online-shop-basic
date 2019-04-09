package com.study.onlineshop.web.servlet;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.service.ServiceLocator;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class ProductsServlet extends HttpServlet {
    private ProductService productService =
            (ProductService) ServiceLocator.getService("productService");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        List<Product> products = productService.getAll();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("products", products);

        String page = pageGenerator.getPage("products", parameters);
        response.getWriter().write(page);
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
