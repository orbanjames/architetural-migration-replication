package com.ecomapp.services.implementation;

import com.ecomapp.dao.*;
import com.ecomapp.models.*;
import com.ecomapp.models.enums.ApplicantStatusEnum;
import com.ecomapp.models.enums.ReviewStatusEnum;
import com.ecomapp.services.ApplicantService;
import com.ecomapp.services.CatalogueService;
import com.ecomapp.services.synod.SynodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public Applicant createApplication(Catalogue catalogue1) {

        Catalogue catalogue = catalogueService.createCatalogue(catalogue1);

        Applicant applicant = createApplicant(catalogue);

        return applicantDAO.save(applicant);
    }

    @Override
    public Boolean activeApplicationNotExist(Catalogue catalogue) {

        List<Applicant> applicants =
                applicantDAO.findByUserAndSynod(
                        catalogue.getSynod().getId(),
                        catalogue.getUser().getId()
                );

        return applicants.stream()
                .map(Applicant::getStatus)
                .allMatch(status -> Objects.equals(status, ApplicantStatusEnum.DECLINED));
    }

    @Override
    public List<Applicant> getByUserAndSynod(int synodId, int userId) {
        return applicantDAO.findByUserAndSynod(synodId, userId);
    }

    @Override
    public List<Applicant> getAll() {
        return applicantDAO.findAll();
    }

    @Override
    public Applicant getByID(int id) {
        return applicantDAO.findById(id).orElse(null);
    }

    @Override
    public Applicant getApplication(int applicantId, int synodId) {
        return applicantDAO.findByIdAndSynod(applicantId, synodId);
    }

    @Override
    public List<ApplicationReport> getReportForApplication(int applicationId) {
        return applicationReportDAO.findByApplication(applicationId);
    }

    @Override
    public ApplicationReport createReport(ApplicationReport report) {
        return applicationReportDAO.save(report);
    }

    @Override
    public void deleteReport(int reportId, int userId) {

        ApplicationReport report =
                applicationReportDAO.findById(reportId).orElse(null);

        if (report != null && report.getUser().getId() == userId) {
            applicationReportDAO.delete(report);
        }
    }

    @Override
    public void closeApplication(int applicantId) {
        applicantDAO.updateStatus(ApplicantStatusEnum.CLOSED.toString(), applicantId);
    }

    @Override
    public void deleteApplication(int applicationId) {
        applicantDAO.deleteById(applicationId);
    }

    @Override
    public void finishApplication(int applicationId) throws Exception {

        List<Review> reviews = reviewDAO.findByApplicant(applicationId);

        long approvedCount = reviews.stream()
                .map(Review::getStatus)
                .filter(status -> status.equals(ReviewStatusEnum.APPROVED))
                .count();

        if (approvedCount >= 2) {
            applicantDAO.updateStatus(ApplicantStatusEnum.APPLIED.toString(), applicationId);
        } else {
            throw new Exception("You must have 2 approvals");
        }
    }

    @Override
    public List<Applicant> getApplicationsForRegistrar(int synodId, int registrarId) throws Exception {

        boolean isRegister = synodService.isUserRegisterToSynod(synodId, registrarId);

        if (!isRegister) {
            throw new Exception("User has not registered for the synod");
        }

        return sectionDAO.findSectionsForRegistrar(registrarId)
                .stream()
                .filter(section ->
                        sectionDAO.isSectionBelongsToSynod(synodId, section.getId()) == 1)
                .flatMap(section ->
                        applicantDAO.findBySynodSection(synodId, section.getId()).stream())
                .filter(this::filterApplicationsForReview)
                .collect(Collectors.toList());
    }

    private boolean filterApplicationsForReview(Applicant applicant) {

        if (applicant.getStatus().equals(ApplicantStatusEnum.OPENED)
                || applicant.getStatus().equals(ApplicantStatusEnum.IN_REVIEW)) {

            List<Review> reviews = reviewDAO.findByApplicant(applicant.getId());

            return reviews.size() < 2;
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