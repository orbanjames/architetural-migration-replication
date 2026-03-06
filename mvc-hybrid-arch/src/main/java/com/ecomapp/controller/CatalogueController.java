package com.ecomapp.controller;

import com.ecomapp.models.Catalogue;
import com.ecomapp.models.CatalogueCategory;
import com.ecomapp.services.CatalogueService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/catalogues")
public class CatalogueController {

    @Resource
    private CatalogueService catalogueService;

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Catalogue>> getCatalogue(@PathVariable int id) {
        return catalogueService.getByID(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}/file")
    public CompletableFuture<ResponseEntity<byte[]>> getCatalogueFileForDownload(
            @PathVariable int id) {

        return catalogueService.getByID(id)
                .thenApply(catalogue -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
                    headers.add(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment;filename=" + catalogue.getFileName());
                    headers.add(HttpHeaders.CONTENT_TYPE,
                            MediaType.APPLICATION_OCTET_STREAM_VALUE);

                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(catalogue.getFile());
                });
    }

    @GetMapping("/categories")
    public CompletableFuture<List<CatalogueCategory>> getCatalogueCategories() {
        return catalogueService.getCategories();
    }

    @GetMapping("/categories/{categoryId}")
    public CompletableFuture<CatalogueCategory> getCatalogueCategory(
            @PathVariable int categoryId) {

        return catalogueService.getCategory(categoryId);
    }
}