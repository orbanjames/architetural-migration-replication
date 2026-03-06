package com.ecomapp.dao;

import com.ecomapp.models.CatalogueCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CatalogueCategoryDAO extends ReactiveCrudRepository<CatalogueCategory, Integer> {

}