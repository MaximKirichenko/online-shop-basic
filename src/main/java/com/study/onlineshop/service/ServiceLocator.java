package com.study.onlineshop.service;

import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.service.impl.DefaultProductService;
import org.sqlite.SQLiteDataSource;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<String, Object> SERVICE_REGISTRY = new HashMap<>();

    static {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:src/main/resources/data.db");

        // configure daos
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        jdbcProductDao.setDataSource(dataSource);

        // configure services
        ProductService productService = new DefaultProductService();

        SERVICE_REGISTRY.put("productDao", jdbcProductDao);
        SERVICE_REGISTRY.put("productService", productService);
    }

    public static Object getService(String serviceName) {
        return SERVICE_REGISTRY.get(serviceName);
    }

}
