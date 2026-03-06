package com.ecomapp.services.implementation.synod;

import com.ecomapp.dao.SectionDAO;
import com.ecomapp.models.SynodSection;
import com.ecomapp.services.synod.SectionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class SectionServiceImpl implements SectionService {

    @Resource
    private SectionDAO sectionDAO;

    @Override
    @Async
    public CompletableFuture<List<SynodSection>> getAll() {
        return CompletableFuture.completedFuture(sectionDAO.findAll());
    }

    @Override
    @Async
    public CompletableFuture<SynodSection> getByID(int id) {
        Optional<SynodSection> section = sectionDAO.findById(id);
        return CompletableFuture.completedFuture(section.orElse(null));
    }

    @Override
    @Async
    public CompletableFuture<List<SynodSection>> getSectionsForSynod(int synodId) {
        return CompletableFuture.completedFuture(sectionDAO.findSectionsForSynod(synodId));
    }

    @Override
    @Async
    public CompletableFuture<List<SynodSection>> getSectionsForCategory(int categoryId) {
        return CompletableFuture.completedFuture(sectionDAO.findSectionsForCategory(categoryId));
    }

    @Override
    @Async
    public CompletableFuture<List<SynodSection>> getSectionsForRegistrar(int registrarId) {
        return CompletableFuture.completedFuture(sectionDAO.findSectionsForRegistrar(registrarId));
    }
}