package com.ecomapp.services.implementation.synod;

import com.ecomapp.dao.SectionDAO;
import com.ecomapp.models.SynodSection;
import com.ecomapp.services.synod.SectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    @Resource
    private SectionDAO sectionDAO;

    @Override
    public List<SynodSection> getAll() {
        return sectionDAO.findAll();
    }

    @Override
    public SynodSection getByID(int id) {
        return sectionDAO.findById(id).orElse(null);
    }

    @Override
    public List<SynodSection> getSectionsForSynod(int synodId) {
        return sectionDAO.findSectionsForSynod(synodId);
    }

    @Override
    public List<SynodSection> getSectionsForCategory(int categoryId) {
        return sectionDAO.findSectionsForCategory(categoryId);
    }

    @Override
    public List<SynodSection> getSectionsForRegistrar(int registrarId) {
        return sectionDAO.findSectionsForRegistrar(registrarId);
    }
}