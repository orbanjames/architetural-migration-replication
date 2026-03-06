package com.ecomapp.dao;

import com.ecomapp.models.ApplicationReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ApplicationReportDAO {

    public static final String BASIC_QUERY = "SELECT rc.* "
            + "FROM application_report rc "
            + "JOIN applicant a on rc.applicant = a.id";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Async
    public CompletableFuture<List<ApplicationReport>> findAll() {
        List<ApplicationReport> result = jdbcTemplate.query(
                BASIC_QUERY,
                new ApplicationReportRowMapper()
        );
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<ApplicationReport>> findByApplication(int applicationId) {
        List<ApplicationReport> result = jdbcTemplate.query(
                BASIC_QUERY + " WHERE rc.applicant = ? ORDER BY rc.dateTime ASC",
                new ApplicationReportRowMapper(),
                applicationId
        );
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<ApplicationReport> findByUser(int id) {
        ApplicationReport result = jdbcTemplate.queryForObject(
                BASIC_QUERY + " WHERE rc.user = ? ORDER BY rc.dateTime ASC",
                new ApplicationReportRowMapper(),
                id
        );
        return CompletableFuture.completedFuture(result);
    }
}