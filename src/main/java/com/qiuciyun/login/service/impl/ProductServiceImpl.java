package com.qiuciyun.login.service.impl;

import com.qiuciyun.login.entity.Product;
import com.qiuciyun.login.mapper.ProductMapper;
import com.qiuciyun.login.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productMapper.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insert(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        product.setUpdateTime(LocalDateTime.now());
        productMapper.update(product);
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.delete(id);
    }
} 