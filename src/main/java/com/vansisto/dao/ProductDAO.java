package com.vansisto.dao;

import com.vansisto.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    int create(Product product);
    Optional<Product> getById(long id);
    int update(Product product);
    int deleteById(long id);

    List<Product> getAll();
}
