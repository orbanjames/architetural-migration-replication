package com.ecomapp.dao;

import com.ecomapp.models.Cart;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

public interface CartDAO extends ReactiveCrudRepository<Cart, Integer> {

    String BASIC_QUERY =
            "SELECT DISTINCT ct.* "
                    + "FROM `cart` ct ";


    @Query(BASIC_QUERY)
    List<Cart> findAll();

    @Query(BASIC_QUERY + " WHERE ct.id = :id")
    Cart findById(@Param("id") int id);

}