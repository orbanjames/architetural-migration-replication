package com.ecomapp.dao;

import com.ecomapp.models.Company;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CompanyDAO extends ReactiveCrudRepository<Company, Integer> {

}
