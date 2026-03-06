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
    public List<Synod> getAllSynods() {
        return synodService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Synod> getSynod(@PathVariable int id) {
        Synod synod = synodService.getByID(id);
        if (synod == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(synod);
    }

    @GetMapping("/{synodId}/users")
    public List<User> getSynodMembers(@PathVariable int synodId) {
        return synodService.getSynodMembers(synodId);
    }

    @GetMapping("/{synodId}/registrars")
    public List<User> getRegistrars(@PathVariable int synodId) {
        return synodService.getRegistrars(synodId);
    }

    @GetMapping("/{synodId}/users/{userId}/is-member")
    public UserStatusDTO getUserSynodStatus(@PathVariable int synodId,
                                            @PathVariable int userId) {

        boolean isRegistered = synodService.isUserRegisterToSynod(synodId, userId);
        String status = userService.getUserSynodStatus(synodId, userId);

        return new UserStatusDTO(isRegistered, status);
    }

    @PostMapping("/{synodId}/users/{userId}/register")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Boolean registerToSynod(@PathVariable int synodId,
                                   @PathVariable int userId) {
        return synodService.registerToSynod(synodId, userId);
    }

    @GetMapping("/{synodId}/sections")
    public List<SynodSection> getSections(@PathVariable int synodId) {
        return sectionService.getSectionsForSynod(synodId);
    }

    @GetMapping("/categories")
    public List<SynodCategory> getCategories() {
        return synodCategoryService.getAll();
    }

    @GetMapping("/categories/{categoryId}")
    public SynodCategory getCategory(@PathVariable int categoryId) {
        return synodCategoryService.getByID(categoryId);
    }
}