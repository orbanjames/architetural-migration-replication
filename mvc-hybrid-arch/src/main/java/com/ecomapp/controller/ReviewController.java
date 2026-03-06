package com.ecomapp.controller;

import com.ecomapp.dto.ResponseDTO;
import com.ecomapp.dto.request.CreateReviewRequestDTO;
import com.ecomapp.models.Review;
import com.ecomapp.models.enums.ReviewStatusEnum;
import com.ecomapp.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @GetMapping("/synods/{synodId}/users/{userId}")
    public CompletableFuture<ResponseEntity<List<Review>>> getReviews(
            @PathVariable int userId,
            @PathVariable int synodId) {

        return reviewService.getReviews(userId, synodId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{reviewId}")
    public CompletableFuture<ResponseEntity<Review>> getReview(
            @PathVariable int reviewId) {

        return reviewService.getByID(reviewId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/{reviewId}/status")
    @PreAuthorize("hasRole('ROLE_REGISTRAR') or hasRole('SYNOD_ADMIN')")
    public CompletableFuture<ResponseEntity<Void>> changeReviewStatus(
            @PathVariable int reviewId,
            @RequestParam final ReviewStatusEnum type) {

        return reviewService.changeStatus(reviewId, type)
                .thenApply(v -> ResponseEntity.ok().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_REGISTRAR') or hasRole('SYNOD_ADMIN')")
    public CompletableFuture<ResponseEntity<ResponseDTO>> createReview(
            @RequestBody CreateReviewRequestDTO review) {

        return reviewService.createReview(
                        review.getApplication(),
                        review.getRegistrar())
                .thenApply(reviewModel ->
                        ResponseEntity.ok(new ResponseDTO(reviewModel, "")))
                .exceptionally(ex ->
                        ResponseEntity.badRequest()
                                .body(new ResponseDTO(null, ex.getMessage())));
    }
}