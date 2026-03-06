package com.ecomapp.dao;

import com.ecomapp.models.Applicant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

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

    public List<Applicant> findAll() {
        return jdbcTemplate.query(
                BASIC_QUERY,
                new ApplicantRowMapper()
        );
    }

    public Applicant findById(int id) {
        return jdbcTemplate.queryForObject(
                JOIN_CATALOGUE_QUERY + " WHERE a.id = ?",
                new ApplicantRowMapper(),
                id
        );
    }

    public Applicant findByIdAndSynod(int id, int synodId) {
        return jdbcTemplate.queryForObject(
                JOIN_CATALOGUE_QUERY + " WHERE a.id = ? AND c.synod = ?",
                new ApplicantRowMapper(),
                id,
                synodId
        );
    }

    public List<Applicant> findByUserAndSynod(int synodId, int userId) {
        return jdbcTemplate.query(
                JOIN_CATALOGUE_QUERY + " WHERE c.synod = ? and c.user = ?",
                new ApplicantRowMapper(),
                synodId,
                userId
        );
    }

    public List<Applicant> findBySynodSection(int synodId, int sectionId) {
        return jdbcTemplate.query(
                JOIN_CATALOGUE_QUERY + " WHERE c.synod = ? and c.section = ?",
                new ApplicantRowMapper(),
                synodId,
                sectionId
        );
    }

    public void updateStatus(String status, int appId) {
        jdbcTemplate.update(
                "UPDATE applicant SET status = ? WHERE id = ?",
                status,
                appId
        );
    }
}