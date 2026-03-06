package com.ecomapp.controller;



import com.ecomapp.models.Country;
import com.ecomapp.services.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Resource
    private CountryService countryService;

    @GetMapping
    public CompletableFuture<List<Country>> getAll() {
        return countryService.getAll();
    }
}
