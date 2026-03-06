package com.ecomapp.services;

import com.ecomapp.models.Applicant;
import com.ecomapp.models.ApplicationReport;
import com.ecomapp.models.Catalogue;

import java.util.List;

public interface ApplicantService extends GenericService<Applicant> {

    Applicant createApplication(Catalogue catalogue);

    Boolean activeApplicationNotExist(Catalogue catalogue);

    List<Applicant> getByUserAndSynod(int synodId, int userId);

    Applicant getApplication(int applicantId, int synodId);

    List<ApplicationReport> getReportForApplication(int applicationId);

    ApplicationReport createReport(ApplicationReport report);

    void deleteReport(int reportId, int userId);

    void closeApplication(int applicationId);

    void deleteApplication(int applicationId);

    void finishApplication(int applicationId);

    List<Applicant> getApplicationsForRegistrar(int synodId, int registrarId);
}