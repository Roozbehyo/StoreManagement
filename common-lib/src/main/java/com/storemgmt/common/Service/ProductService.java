package com.storemgmt.common.Service;

import com.storemgmt.common.Entity.Product;
import com.storemgmt.common.Repo.ProductRepository;

import java.util.List;

public class ProductService implements Service<Product, Integer> {
    @Override
    public void save(Product product) throws Exception {
        if (product.getPrice() < 0) {
            throw new Exception("Price cannot be negative");
        }
        try (ProductRepository productRepository = new ProductRepository()) {
            productRepository.save(product);
        }
    }

    @Override
    public void edit(Product product) throws Exception {
        findById(product.getId());
        if (product.getPrice() < 0) {
            throw new Exception("Price cannot be negative");
        }
        try (ProductRepository productRepository = new ProductRepository()) {
            productRepository.save(product);
        }
    }

    @Override
    public void remove(Integer id) throws Exception {
        findById(id);
        try (ProductRepository productRepository = new ProductRepository()) {
            productRepository.remove(id);
        }
    }

    @Override
    public List<Product> findAll() throws Exception {
        try (ProductRepository productRepository = new ProductRepository()) {
            List<Product> productList = productRepository.findAll();
            if (productList.isEmpty()) {
                throw new Exception("Product not found");
            }
            return productList;
        }
    }

    @Override
    public Product findById(Integer id) throws Exception {
        try (ProductRepository productRepository = new ProductRepository()) {
            Product product = productRepository.findById(id);
            if (product == null) {
                throw new Exception("Product not found");
            }
            return product;
        }
    }

    public List<Product> findAllByName(String name) throws Exception {
        try (ProductRepository productRepository = new ProductRepository()) {
            List<Product> productList = productRepository.findAllByName(name);
            if (productList.isEmpty()) {
                throw new Exception("Product not found");
            }
            return productList;
        }
    }
}
