package com.ecomapp.dao;

import com.ecomapp.models.Country;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryDAO extends ReactiveCrudRepository<Country, Integer> {

}

