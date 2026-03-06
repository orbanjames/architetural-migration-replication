package com.ecomapp.services.implementation;

import com.ecomapp.dao.CountryDAO;
import com.ecomapp.models.Country;
import com.ecomapp.services.CountryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class CountryServiceImpl implements CountryService {

    @Resource
    private CountryDAO countryDAO;

    @Override
    public Flux<Country> getAll() {
        return countryDAO.findAll();
    }

    @Override
    public Mono<Country> getByID(int id) {
        return countryDAO.findById(id);
    }
}
