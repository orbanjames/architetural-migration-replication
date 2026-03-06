package com.ecomapp.services;

import com.ecomapp.models.Applicant;
import com.ecomapp.models.ApplicationReport;
import com.ecomapp.models.Catalogue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApplicantService extends GenericService<Applicant> {

    Mono<Applicant> createApplication(Catalogue catalogue);

    Mono<Boolean> activeApplicationNotExist(Catalogue catalogue);

    Flux<Applicant> getByUserAndSynod(int synodId, int userId);

    Mono<Applicant> getApplication(int applicantId, int synodId);

    Flux<ApplicationReport> getReportForApplication(int applicationId);

    Mono<ApplicationReport> createReport(ApplicationReport report);

    Mono<Void> deleteReport(int reportId, int userId);

    Mono<Void> closeApplication(int applicationId);

    Mono<Void> deleteApplication(int applicationId);

    Mono<Void> finishApplication(int applicationId);

    Flux<Applicant> getApplicationsForRegistrar(int synodId, int registrarId);
}
