package com.study.onlineshop;

import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.service.ProductService;
import com.study.onlineshop.service.impl.DefaultProductService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.sqlite.SQLiteDataSource;

public class Starter {
    public static void main(String[] args) throws Exception {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:src/main/resources/data.db");

        // configure daos
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        jdbcProductDao.setDataSource(dataSource);

        // configure services
        ProductService productService = new DefaultProductService(jdbcProductDao);

        // servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(productService);

        DeleteProductServlet deleteProductServlet = new DeleteProductServlet();
        deleteProductServlet.setProductService(productService);

        AddProductServlet addProductServlet = new AddProductServlet();
        addProductServlet.setProductService(productService);

        EditProductServlet editProductServlet = new EditProductServlet();
        editProductServlet.setProductService(productService);

        // config web server
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/");
        servletContextHandler.addServlet(new ServletHolder(deleteProductServlet), "/product/delete/*");
        servletContextHandler.addServlet(new ServletHolder(addProductServlet), "/product/add");
        servletContextHandler.addServlet(new ServletHolder(editProductServlet), "/product/edit/*");

        Server server = new Server(8080);
        server.setHandler(servletContextHandler);
        server.start();
    }

}
