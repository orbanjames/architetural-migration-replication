package com.ecomapp.dao;

import com.ecomapp.models.Applicant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ApplicantDAO {

    public static final String BASIC_QUERY = "SELECT a.* FROM applicant a";

    public static final String JOIN_CATALOGUE_QUERY =
            "SELECT a.*, c.name as catalogueName, c.description as catalogueDescription, c.createdAt as catalogueCreatedAt, "
                    + "c.file as catalogueFile, c.fileName as catalogueFileName, c.user, c.synod, c.catalogueCategory, c.section "
                    + "FROM applicant a "
                    + "JOIN catalogue c on a.catalogue = c.id";

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Async
    public CompletableFuture<List<Applicant>> findAll() {
        List<Applicant> result = jdbcTemplate.query(
                BASIC_QUERY,
                new ApplicantRowMapper()
        );
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<Applicant> findById(int id) {
        Applicant result = jdbcTemplate.queryForObject(
                JOIN_CATALOGUE_QUERY + " WHERE a.id = ?",
                new ApplicantRowMapper(),
                id
        );
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<Applicant> findByIdAndSynod(int id, int synodId) {
        Applicant result = jdbcTemplate.queryForObject(
                JOIN_CATALOGUE_QUERY + " WHERE a.id = ? AND c.synod = ?",
                new ApplicantRowMapper(),
                id,
                synodId
        );
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<Applicant>> findByUserAndSynod(int synodId, int userId) {
        List<Applicant> result = jdbcTemplate.query(
                JOIN_CATALOGUE_QUERY + " WHERE c.synod = ? and c.user = ?",
                new ApplicantRowMapper(),
                synodId,
                userId
        );
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<Applicant>> findBySynodSection(int synodId, int sectionId) {
        List<Applicant> result = jdbcTemplate.query(
                JOIN_CATALOGUE_QUERY + " WHERE c.synod = ? and c.section = ?",
                new ApplicantRowMapper(),
                synodId,
                sectionId
        );
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<Void> updateStatus(String status, int appId) {
        jdbcTemplate.update(
                "UPDATE applicant SET status = ? WHERE id = ?",
                status,
                appId
        );
        return CompletableFuture.completedFuture(null);
    }
}