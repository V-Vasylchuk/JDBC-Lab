package com.vansisto.servlet;

import com.vansisto.model.Product;
import com.vansisto.service.ProductService;
import com.vansisto.service.impl.ProductServiceImpl;
import com.vansisto.util.RestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private ProductService productService = new ProductServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = RestUtil.getFromJson(req, Product.class);
        int status = productService.create(product);
        resp.setStatus(status);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String respJson = RestUtil.toJson(productService.getAll());
        resp.getWriter().write(respJson);
    }
}
