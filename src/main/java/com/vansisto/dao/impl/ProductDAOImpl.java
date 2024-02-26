package com.vansisto.dao.impl;

import com.vansisto.config.MySQLConnector;
import com.vansisto.dao.ProductDAO;
import com.vansisto.exeption.DataProcessingException;
import com.vansisto.model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public int create(Product product) {
        int status = 400;
        String SQL = "INSERT INTO product(name, price) VALUES (?, ?)";

        try (Connection connection = MySQLConnector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setBigDecimal(2, product.getPrice());

            preparedStatement.execute();
            status = 201;
        } catch (SQLException e) {
            throw new RuntimeException("Can't create a product! " + e);
        }
        return status;
    }

    @Override
    public Optional<Product> getById(long id) {
        String getQuery = "SELECT * FROM product WHERE id = ?";
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement getProductStatement = connection.prepareStatement(getQuery)) {
            getProductStatement.setLong(1, id);
            ResultSet resultSet = getProductStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get product from DB with id:" + id, e);
        }
        return Optional.empty();
    }

    @Override
    public int update(Product product) {
        int status = 400;
        String updateQuery = "UPDATE product SET name = ?, price = ? WHERE id = ?";

        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement updateProductStatement = connection.prepareStatement(updateQuery)) {
            updateProductStatement.setString(1, product.getName());
            updateProductStatement.setBigDecimal(2, product.getPrice());
            updateProductStatement.setLong(3, product.getId());
            updateProductStatement.executeUpdate();
            status = 202;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update product from DB!", e);
        }
        return status;
    }

    @Override
    public int deleteById(long id) {
        int status = 400;
        String deleteQuery = "DELETE FROM product WHERE id = ?"; //better use soft delete but for example ok;
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement deleteProductStatement = connection.prepareStatement(deleteQuery)) {
            deleteProductStatement.setLong(1, id);
            deleteProductStatement.execute();
            status = 202;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete product with id: " + id, e);
        }
        return status;
    }

    @Override
    public List<Product> getAll() {
        List<Product> productList = new ArrayList<>();
        String getAllQuery = "SELECT * FROM product";

        try (Connection connection = MySQLConnector.getConnection();
                PreparedStatement getAllStatement = connection.prepareStatement(getAllQuery)) {
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
                Product product = createProduct(resultSet);
                productList.add(product);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products from DB", e);
        }
        return productList;
    }

    private Product createProduct(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");

        return new Product(id, name, price);
    }
}
