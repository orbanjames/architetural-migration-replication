package com.ecomapp.dao;

import com.ecomapp.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

public interface ProductDAO <Product, Integer> {

    String BASIC_QUERY =
            "SELECT p.*, c.name as 'categoryName', com.name as 'companyName', col.type as 'colorType' "
                    + "FROM `product` p "
                    + "JOIN color col on p.color = col.id "
                    + "JOIN company com on p.company = com.id "
                    + "JOIN category c on p.category = c.id";

    @Query(BASIC_QUERY)
    List<Product> findAll();

    @Query(BASIC_QUERY + " WHERE p.id = :id")
    Product findById(@Param("id") int id);
}
