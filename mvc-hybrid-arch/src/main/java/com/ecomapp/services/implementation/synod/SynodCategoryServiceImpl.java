package com.ecomapp.services.implementation.synod;

import com.ecomapp.dao.SynodCategoryDAO;
import com.ecomapp.models.SynodCategory;
import com.ecomapp.services.synod.SynodCategoryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class SynodCategoryServiceImpl implements SynodCategoryService {

    @Resource
    private SynodCategoryDAO categoryDAO;

    @Override
    public CompletableFuture<List<SynodCategory>> getAll() {
        return CompletableFuture.completedFuture(categoryDAO.findAll());
    }

    @Override
    public CompletableFuture<SynodCategory> getByID(int id) {
        Optional<SynodCategory> category = categoryDAO.findById(id);
        return CompletableFuture.completedFuture(category.orElse(null));
    }
}