package com.ecomapp.dao;


import com.ecomapp.models.Color;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ColorDAO extends ReactiveCrudRepository<Color, Integer> {

}
