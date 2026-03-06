package com.ecomapp.controller.User;

import com.ecomapp.models.Title;
import com.ecomapp.services.TitleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/users/titles")
public class UserTitleController {

    @Resource
    private TitleService titleService;

    @GetMapping
    public List<Title> getAll() {
        return titleService.getAll();
    }
}