package com.ecomapp.services.implementation;


import com.ecomapp.dao.ProductCategoryDAO;
import com.ecomapp.dao.ColorDAO;
import com.ecomapp.dao.CompanyDAO;
import com.ecomapp.dao.ProductDAO;
import com.ecomapp.models.*;
import com.ecomapp.services.ProductService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDAO productDAO;
    @Resource
    private ProductCategoryDAO productCategoryDAO;
    @Resource
    private CompanyDAO companyDAO;
    @Resource
    private ColorDAO colorDAO;
    @Override
    public Flux<Product> getAll() {
        return productDAO.findAll();
    }
    @Override
    public Mono<Product> getByID(int id) {
        return productDAO.findById(id);
    }
    @Override
    public Flux<ProductCategory> getCategories() {
        return productCategoryDAO.findAll();
    }
    @Override
    public Mono<ProductCategory> getCategory(int categoryId) {
        return productCategoryDAO.findById(categoryId);
    }
    @Override
    public Flux<Company> getCompanies() {
        return companyDAO.findAll();
    }

    @Override
    public Mono<Company> getCompany(int companyId) {
        return companyDAO.findById(companyId);
    }

    @Override
    public Flux<Color> getColors() {
        return colorDAO.findAll();
    }

    @Override
    public Mono<Color> getColor(int colorId) {
        return colorDAO.findById(colorId);
    }

    @Override
    public Mono<Product> deleteProduct(int id) {
        return productDAO.findById(id);
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return productDAO.save(product);
    }


}

