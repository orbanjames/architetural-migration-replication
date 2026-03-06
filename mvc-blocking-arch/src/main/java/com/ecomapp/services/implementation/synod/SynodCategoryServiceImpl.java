package com.ecomapp.services.implementation.synod;

import com.ecomapp.dao.SynodCategoryDAO;
import com.ecomapp.models.SynodCategory;
import com.ecomapp.services.synod.SynodCategoryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class SynodCategoryServiceImpl implements SynodCategoryService {

    @Resource
    private SynodCategoryDAO categoryDAO;

    @Override
    public Flux<SynodCategory> getAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Mono<SynodCategory> getByID(int id) {
        return categoryDAO.findById(id);
    }
}