package com.ecomapp.dao;

import com.ecomapp.models.Synod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

public class SynodDAO {

    String BASIC_QUERY =
            "SELECT synod.*, c.name as countryName, c.isoCode as countryIsoCode, cc.name as categoryName, cc.description as categoryDesc "
                    + "FROM `synod` synod JOIN country c ON synod.country = c.id join synod_category cc on synod.synodCategory = cc.id";

    @Query(BASIC_QUERY)
    List<Synod> findAll();

    @Query(BASIC_QUERY + " WHERE synod.id = :id")
    Synod findById(@Param("id") int id);

    @Query(BASIC_QUERY + " WHERE c.id = :countryId")
    Synod findByCountry(@Param("countryId") int countryId);

    @Query("SELECT count(1) FROM `register_to_synod` WHERE user = :userId AND synod = :synodId")
    Integer isUserRegisterToSynod(@Param("synodId") int synodId, @Param("userId") int userId);

    @Query("INSERT INTO `register_to_synod`(user, synod, status) VALUES (:userId,:synodId,:status)")
    Integer registerToSynod(@Param("synodId") int synod, @Param("userId") int user, @Param("status") String status);

    @Query("SELECT synod.*, c.name as countryName, c.isoCode as countryIsoCode, sc.name as categoryName, sc.description as categoryDesc "
            + "FROM `register_to_synod` rc "
            + "JOIN synod synod on synod.id = rc.synod "
            + "JOIN Country c ON synod.country = c.id "
            + "JOIN synod_category sc on synod.synodCategory = sc.id "
            + "WHERE user = :userId")
    List<Synod> findSynodForUser(@Param("userId") int userId);
}