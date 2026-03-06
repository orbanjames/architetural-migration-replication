package com.ecomapp.dao;

import com.ecomapp.models.ApplicationReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ApplicationReportDAO {

    public static final String BASIC_QUERY = "SELECT rc.* "
            + "FROM application_report rc "
            + "JOIN applicant a on rc.applicant = a.id";

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<ApplicationReport> findAll() {
        return jdbcTemplate.query(
                BASIC_QUERY,
                new ApplicationReportRowMapper()
        );
    }

    public List<ApplicationReport> findByApplication(int applicationId) {
        return jdbcTemplate.query(
                BASIC_QUERY + " WHERE rc.applicant = ? ORDER BY rc.dateTime ASC",
                new ApplicationReportRowMapper(),
                applicationId
        );
    }

    public ApplicationReport findByUser(int id) {
        return jdbcTemplate.queryForObject(
                BASIC_QUERY + " WHERE rc.user = ? ORDER BY rc.dateTime ASC",
                new ApplicationReportRowMapper(),
                id
        );
    }
}