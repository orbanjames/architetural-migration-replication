package com.ecomapp.services.implementation;

import com.ecomapp.dao.CatalogueCategoryDAO;
import com.ecomapp.dao.CatalogueDAO;
import com.ecomapp.models.Catalogue;
import com.ecomapp.models.CatalogueCategory;
import com.ecomapp.services.CatalogueService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    @Resource
    private CatalogueDAO catalogueDAO;

    @Resource
    private CatalogueCategoryDAO catalogueCategoryDAO;

    @Override
    public Flux<Catalogue> getAll() {
        return catalogueDAO.findAll();
    }

    @Override
    public Mono<Catalogue> getByID(int id) {
        return catalogueDAO.findById(id);
    }

    @Override
    public Flux<CatalogueCategory> getCategories() {
        return catalogueCategoryDAO.findAll();
    }

    @Override
    public Mono<CatalogueCategory> getCategory(int categoryId) {
        return catalogueCategoryDAO.findById(categoryId);
    }

    @Override
    public Mono<Catalogue> createCatalogue(Catalogue catalogue) {
        return catalogueDAO.save(catalogue);
    }
}
