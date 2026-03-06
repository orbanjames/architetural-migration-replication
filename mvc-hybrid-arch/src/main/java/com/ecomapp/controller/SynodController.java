package com.ecomapp.controller;

import com.ecomapp.dto.UserStatusDTO;
import com.ecomapp.models.*;
import com.ecomapp.services.UserService;
import com.ecomapp.services.synod.SectionService;
import com.ecomapp.services.synod.SynodCategoryService;
import com.ecomapp.services.synod.SynodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/synods")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SynodController {

    @Resource
    private SynodService synodService;

    @Resource
    private UserService userService;

    @Resource
    private SynodCategoryService synodCategoryService;

    @Resource
    private SectionService sectionService;

    @GetMapping
    public CompletableFuture<List<Synod>> getAllSynods() {
        return synodService.getAll();
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Synod>> getSynod(@PathVariable int id) {
        return synodService.getByID(id)
                .thenApply(synod -> synod == null ?
                        ResponseEntity.notFound().build()
                        : ResponseEntity.ok(synod));
    }

    @GetMapping("/{synodId}/users")
    public CompletableFuture<List<User>> getSynodMembers(@PathVariable int synodId) {
        return synodService.getSynodMembers(synodId);
    }

    @GetMapping("/{synodId}/registrars")
    public CompletableFuture<List<User>> getRegistrars(@PathVariable int synodId) {
        return synodService.getRegistrars(synodId);
    }

    @GetMapping("/{synodId}/users/{userId}/is-member")
    public CompletableFuture<UserStatusDTO> getUserSynodStatus(
            @PathVariable int synodId,
            @PathVariable int userId) {

        CompletableFuture<Boolean> registeredFuture =
                synodService.isUserRegisterToSynod(synodId, userId);

        CompletableFuture<String> statusFuture =
                userService.getUserSynodStatus(synodId, userId);

        return registeredFuture.thenCombine(statusFuture,
                (isRegistered, status) ->
                        new UserStatusDTO(isRegistered, status));
    }

    @PostMapping("/{synodId}/users/{userId}/register")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CompletableFuture<Boolean> registerToSynod(
            @PathVariable int synodId,
            @PathVariable int userId) {

        return synodService.registerToSynod(synodId, userId);
    }

    @GetMapping("/{synodId}/sections")
    public CompletableFuture<List<SynodSection>> getSections(
            @PathVariable int synodId) {

        return sectionService.getSectionsForSynod(synodId);
    }

    @GetMapping("/categories")
    public CompletableFuture<List<SynodCategory>> getCategories() {
        return synodCategoryService.getAll();
    }

    @GetMapping("/categories/{categoryId}")
    public CompletableFuture<SynodCategory> getCategory(
            @PathVariable int categoryId) {

        return synodCategoryService.getByID(categoryId);
    }
}