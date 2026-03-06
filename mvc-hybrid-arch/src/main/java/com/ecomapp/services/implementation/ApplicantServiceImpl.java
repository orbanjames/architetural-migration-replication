package com.ecomapp.services.implementation;

import com.ecomapp.dao.*;
import com.ecomapp.models.*;
import com.ecomapp.models.enums.ApplicantStatusEnum;
import com.ecomapp.models.enums.ReviewStatusEnum;
import com.ecomapp.services.ApplicantService;
import com.ecomapp.services.CatalogueService;
import com.ecomapp.services.synod.SynodService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    @Resource
    private ApplicantDAO applicantDAO;

    @Resource
    private CatalogueService catalogueService;

    @Resource
    private ApplicationReportDAO applicationReportDAO;

    @Resource
    private ReviewDAO reviewDAO;

    @Resource
    private SectionDAO sectionDAO;

    @Resource
    private SynodService synodService;

    @Async
    @Override
    public CompletableFuture<Applicant> createApplication(Catalogue catalogue1) {

        Catalogue catalogue = catalogueService.createCatalogue(catalogue1);

        return CompletableFuture.completedFuture(
                applicantDAO.save(createApplicant(catalogue)));
    }

    @Async
    @Override
    public CompletableFuture<Boolean> activeApplicationNotExist(Catalogue catalogue) {

        List<Applicant> applicants =
                applicantDAO.findByUserAndSynod(
                        catalogue.getSynod().getId(),
                        catalogue.getUser().getId());

        Boolean result = applicants.stream()
                .map(Applicant::getStatus)
                .allMatch(status -> Objects.equals(status, ApplicantStatusEnum.DECLINED));

        return CompletableFuture.completedFuture(result);
    }

    @Async
    @Override
    public CompletableFuture<List<Applicant>> getByUserAndSynod(int synodId, int userId) {
        return CompletableFuture.completedFuture(
                applicantDAO.findByUserAndSynod(synodId, userId));
    }

    @Async
    @Override
    public CompletableFuture<List<Applicant>> getAll() {
        return CompletableFuture.completedFuture(applicantDAO.findAll());
    }

    @Async
    @Override
    public CompletableFuture<Applicant> getByID(int id) {
        return CompletableFuture.completedFuture(
                applicantDAO.findById(id).orElse(null));
    }

    @Async
    @Override
    public CompletableFuture<Applicant> getApplication(int applicantId, int synodId) {
        return CompletableFuture.completedFuture(
                applicantDAO.findByIdAndSynod(applicantId, synodId));
    }

    @Async
    @Override
    public CompletableFuture<List<ApplicationReport>> getReportForApplication(int applicationId) {
        return CompletableFuture.completedFuture(
                applicationReportDAO.findByApplication(applicationId));
    }

    @Async
    @Override
    public CompletableFuture<ApplicationReport> createReport(ApplicationReport report) {
        return CompletableFuture.completedFuture(
                applicationReportDAO.save(report));
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteReport(int reportId, int userId) {

        ApplicationReport report = applicationReportDAO.findById(reportId).orElse(null);

        if (report != null && report.getUser().getId() == userId) {
            applicationReportDAO.delete(report);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Void> closeApplication(int applicantId) {
        applicantDAO.updateStatus(ApplicantStatusEnum.CLOSED.toString(), applicantId);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteApplication(int applicationId) {
        applicantDAO.deleteById(applicationId);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Void> finishApplication(int applicationId) {

        List<Review> reviews = reviewDAO.findByApplicant(applicationId);

        long approvedCount = reviews.stream()
                .filter(r -> r.getStatus().equals(ReviewStatusEnum.APPROVED))
                .count();

        if (approvedCount >= 2) {
            applicantDAO.updateStatus(ApplicantStatusEnum.APPLIED.toString(), applicationId);
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.failedFuture(
                new RuntimeException("You must have 2 approvals"));
    }

    @Async
    @Override
    public CompletableFuture<List<Applicant>> getApplicationsForRegistrar(int synodId, int registrarId) {

        boolean isRegister = synodService.isUserRegisterToSynod(synodId, registrarId);

        if (!isRegister) {
            return CompletableFuture.failedFuture(
                    new RuntimeException("User is not has not register for the synod"));
        }

        List<Applicant> result =
                sectionDAO.findSectionsForRegistrar(registrarId)
                        .stream()
                        .filter(section -> isSectionBelongsToSynod(synodId, section.getId()))
                        .flatMap(section -> applicantDAO.findBySynodSection(synodId, section.getId()).stream())
                        .filter(this::filterApplicationsForReview)
                        .toList();

        return CompletableFuture.completedFuture(result);
    }

    private boolean isSectionBelongsToSynod(int synodId, int sectionId) {
        Integer count = sectionDAO.isSectionBelongsToSynod(synodId, sectionId);
        return count.equals(1);
    }

    private boolean filterApplicationsForReview(Applicant application) {

        if (application.getStatus().equals(ApplicantStatusEnum.OPENED)
                || application.getStatus().equals(ApplicantStatusEnum.IN_REVIEW)) {

            int count = reviewDAO.findByApplicant(application.getId()).size();

            return count < 2;
        }

        return false;
    }

    private Applicant createApplicant(Catalogue catalogue) {

        Applicant applicant = new Applicant();

        applicant.setCatalogue(catalogue);
        applicant.setSynodMember(catalogue.getUser());
        applicant.setStatus(ApplicantStatusEnum.OPENED);

        return applicant;
    }
}