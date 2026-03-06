package com.ecomapp.services.implementation;

import com.ecomapp.dao.ApplicantDAO;
import com.ecomapp.dao.ReviewDAO;
import com.ecomapp.models.Applicant;
import com.ecomapp.models.Review;
import com.ecomapp.models.User;
import com.ecomapp.models.enums.ApplicantStatusEnum;
import com.ecomapp.models.enums.ReviewStatusEnum;
import com.ecomapp.services.ReviewService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewDAO reviewDAO;

    @Resource
    private ApplicantDAO applicantDAO;

    @Override
    public Flux<Review> getAll() {
        return reviewDAO.findAll();
    }

    @Override
    public Mono<Review> getByID(int id) {
        return reviewDAO.findById(id);
    }

    @Override
    public Mono<Review> getReview(int registrarId, int applicantID) {
        return reviewDAO.findByRegistrarIdAndApplicant(registrarId, applicantID);
    }

    @Override
    public Flux<Review> getReviews(int synodRegistrar, int synodId) {
        return reviewDAO.findBySynodRegistrar(synodRegistrar, synodId);
    }

    @Override
    public Flux<Review> getReviews(int registrar, int synodId, String applicantStatus) {
        return reviewDAO.findByRegistrarIdAndApplicantStatus(registrar, synodId, applicantStatus);
    }

    @Override
    public Flux<Review> getReviews(int applicantID) {
        return reviewDAO.findByApplicant(applicantID);
    }

    @Override
    public Mono<Void> changeStatus(int reviewId, ReviewStatusEnum status) {
        return reviewDAO.updateStatus(status.toString(), reviewId);
    }

    @Override
    public Mono<Review> createReview(Applicant application, User registrar) {
        Review review = new Review();
        review.setApplicant(application);
        review.setRegistrar(registrar);
        review.setStatus(ReviewStatusEnum.IN_PROGRESS);
        return reviewDAO.save(review)
                .onErrorResume(e -> {
                    if (e instanceof DataIntegrityViolationException) {
                        return Mono.error(new DataIntegrityViolationException(
                                "User is already reviewed"));
                    } else {
                        return Mono.error(e);
                    }
                })
                .doOnNext(Review -> applicantDAO.updateStatus(ApplicantStatusEnum.IN_REVIEW.toString(),
                        application.getId()).subscribe());
    }
}
