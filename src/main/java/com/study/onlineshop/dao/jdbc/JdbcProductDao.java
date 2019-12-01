package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.dao.jdbc.mapper.ProductRowMapper;
import com.study.onlineshop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SqlResolve")
@Repository
public class JdbcProductDao implements ProductDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RowMapper<Product> rowMapper;

    @Override
    public List<Product> getAll() {
        String query = "SELECT id, name, price FROM product";
        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public Product getById(int id) {
        String query = "SELECT id, name, price FROM product WHERE id = ?";
        return jdbcTemplate.query(query, rowMapper, id).stream().findFirst().orElse(null);
    }

    @Override
    public int add(Product product) {
        String query = "INSERT INTO product(name, price) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            return preparedStatement;
        }, keyHolder);

        return (int)keyHolder.getKey();
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM product WHERE id = ?;";
        jdbcTemplate.update(query, id);
    }

    @Override
    public void update(Product product) {
        String query = "UPDATE product SET name = ?, price = ? WHERE id = ?;";
        jdbcTemplate.update(query, ps -> {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getId());
        });
    }
}
