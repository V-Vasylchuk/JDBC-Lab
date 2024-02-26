package com.vansisto.servlet;

import com.vansisto.service.ProductService;
import com.vansisto.service.impl.ProductServiceImpl;
import com.vansisto.util.RestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/products")
public class ProductsServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String respJson = RestUtil.toJson(productService.getAll());
        resp.getWriter().write(respJson);
    }
}
