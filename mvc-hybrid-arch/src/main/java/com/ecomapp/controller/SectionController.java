package com.ecomapp.controller;

import com.ecomapp.models.SynodSection;
import com.ecomapp.services.synod.SectionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Resource
    private SectionService sectionService;

    @GetMapping("/{sectionId}")
    public CompletableFuture<SynodSection> getSection(@PathVariable int sectionId) {
        return sectionService.getByID(sectionId);
    }

    @GetMapping("/categories/{categoryId}")
    public CompletableFuture<List<SynodSection>> getSectionsOfCategory(
            @PathVariable int categoryId) {
        return sectionService.getSectionsForCategory(categoryId);
    }

    @GetMapping("/registrar/{registrarId}")
    public CompletableFuture<List<SynodSection>> getSectionsForRegistrar(
            @PathVariable int registrarId) {
        return sectionService.getSectionsForRegistrar(registrarId);
    }
}