package com.ecomapp.dao;

import com.ecomapp.models.Title;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TitleDAO extends ReactiveCrudRepository<Title, Integer> {

}
