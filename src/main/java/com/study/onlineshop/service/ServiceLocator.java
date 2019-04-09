package com.study.onlineshop.service;

import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.service.impl.DefaultProductService;
import org.sqlite.SQLiteDataSource;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<String, Object> SERVICES = new HashMap<>();

    static {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:src/main/resources/data.db");

//        // configure daos
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        jdbcProductDao.setDataSource(dataSource);
//
//        // configure services
        ProductService productService = new DefaultProductService(jdbcProductDao);

        register("productService", productService);
    }

    public static void register(String serviceName, Object service) {
        SERVICES.put(serviceName, service);
    }

    public static Object getService(String serviceName) {
        return SERVICES.get(serviceName);
    }
}
