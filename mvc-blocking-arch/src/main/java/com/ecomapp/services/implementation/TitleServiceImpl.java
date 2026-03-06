package com.ecomapp.services.implementation;

import com.ecomapp.dao.TitleDAO;
import com.ecomapp.models.Title;
import com.ecomapp.services.TitleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class TitleServiceImpl implements TitleService {

    @Resource
    private TitleDAO titleDAO;

    @Override
    public Flux<Title> getAll() {
        return titleDAO.findAll();
    }

    @Override
    public Mono<Title> getByID(int id) {
        return titleDAO.findById(id);
    }
}