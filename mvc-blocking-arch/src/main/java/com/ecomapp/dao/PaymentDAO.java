package com.ecomapp.dao;

import com.ecomapp.models.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

public interface PaymentDAO extends ReactiveCrudRepository<Payment, Integer> {

    String BASIC_QUERY = "SELECT p.* FROM payment p";

    @Query(BASIC_QUERY + " WHERE p.status = :status")
    Payment findByStatus(@Param("status") String status);
}

