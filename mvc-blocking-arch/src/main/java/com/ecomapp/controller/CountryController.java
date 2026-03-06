package com.ecomapp.controller;



import com.ecomapp.models.Country;
import com.ecomapp.services.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Resource
    private CountryService countryService;

    @GetMapping
    public List<Country> getAll() {
        return countryService.getAll();
    }
}
