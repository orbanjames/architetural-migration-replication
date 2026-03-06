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

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @GetMapping("/synods/{synodId}/users/{userId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable int userId,
                                                   @PathVariable int synodId) {
        return ResponseEntity.ok(reviewService.getReviews(userId, synodId));
    }

    @GetMapping("/applications/{applicantId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable int applicantId) {
        return ResponseEntity.ok(reviewService.getReviews(applicantId));
    }

    @GetMapping("/synods/{synodId}/registrar/{registrarId}/applicant-status/{status}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable int synodId,
                                                   @PathVariable int registrarId,
                                                   @PathVariable String status) {
        return ResponseEntity.ok(reviewService.getReviews(registrarId, synodId, status));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable int reviewId) {
        return ResponseEntity.ok(reviewService.getByID(reviewId));
    }

    @GetMapping("/applications/{applicantId}/registrar/{registrarId}")
    public ResponseEntity<Review> getReview(@PathVariable int applicantId,
                                            @PathVariable int registrarId) {
        return ResponseEntity.ok(reviewService.getReview(registrarId, applicantId));
    }

    @PostMapping("/{reviewId}/status")
    @PreAuthorize("hasRole('ROLE_REGISTRAR') or hasRole('SYNOD_ADMIN')")
    public ResponseEntity<Void> changeReviewStatus(@PathVariable int reviewId,
                                                   @RequestParam final ReviewStatusEnum type) {
        reviewService.changeStatus(reviewId, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_REGISTRAR') or hasRole('SYNOD_ADMIN')")
    public ResponseEntity<ResponseDTO> createReview(
            @RequestBody CreateReviewRequestDTO review) {

        try {
            Review reviewModel = reviewService.createReview(
                    review.getApplication(),
                    review.getRegistrar()
            );

            return ResponseEntity.ok(new ResponseDTO(reviewModel, ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(null, e.getMessage()));
        }
    }
}