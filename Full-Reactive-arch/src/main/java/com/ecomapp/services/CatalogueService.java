package com.ecomapp.services;

import com.ecomapp.models.Catalogue;
import com.ecomapp.models.CatalogueCategory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CatalogueService extends GenericService<Catalogue> {

    Flux<CatalogueCategory> getCategories();

    Mono<CatalogueCategory> getCategory(int categoryId);

    Mono<Catalogue> createCatalogue(Catalogue catalogue);
}
