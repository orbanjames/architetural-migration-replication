package com.ecomapp.dao;



import com.ecomapp.models.ProductCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductCategoryDAO extends ReactiveCrudRepository<ProductCategory, Integer> {

}
