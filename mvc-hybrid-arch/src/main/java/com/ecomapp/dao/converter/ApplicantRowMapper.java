package com.ecomapp.dao.converter;

import com.ecomapp.models.Applicant;
import com.ecomapp.models.User;
import com.ecomapp.models.enums.ApplicantStatusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicantRowMapper implements RowMapper<Applicant> {

    @Override
    public Applicant mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Applicant.builder()
                .id(rs.getInt("id"))
                .catalogue(ReviewReadConverter.getCatalogue(rs))
                .synodMember(User.builder().id(rs.getInt("synodMember")).build())
                .status(ApplicantStatusEnum.valueOf(rs.getString("status")))
                .build();
    }
}