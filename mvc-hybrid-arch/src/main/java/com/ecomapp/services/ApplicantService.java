package com.ecomapp.services;

import com.ecomapp.models.Applicant;
import com.ecomapp.models.ApplicationReport;
import com.ecomapp.models.Catalogue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApplicantService extends GenericService<Applicant> {

    CompletableFuture<Applicant> createApplication(Catalogue catalogue);

    CompletableFuture<Boolean> activeApplicationNotExist(Catalogue catalogue);

    CompletableFuture<List<Applicant>> getByUserAndSynod(int synodId, int userId);

    CompletableFuture<Applicant> getApplication(int applicantId, int synodId);

    CompletableFuture<List<ApplicationReport>> getReportForApplication(int applicationId);

    CompletableFuture<ApplicationReport> createReport(ApplicationReport report);

    CompletableFuture<Void> deleteReport(int reportId, int userId);

    CompletableFuture<Void> closeApplication(int applicationId);

    CompletableFuture<Void> deleteApplication(int applicationId);

    CompletableFuture<Void> finishApplication(int applicationId);

    CompletableFuture<List<Applicant>> getApplicationsForRegistrar(int synodId, int registrarId);
}