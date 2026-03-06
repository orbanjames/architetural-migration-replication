package com.ecomapp.dao.converter;

import com.ecomapp.models.Applicant;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ApplicantWriteMapper {

    public static void mapToPreparedStatement(PreparedStatement ps, Applicant applyModel) throws SQLException {

        ps.setInt(1, applyModel.getSynodMember().getId());
        ps.setInt(2, applyModel.getCatalogue().getId());
        ps.setString(3, applyModel.getStatus().toString());
    }
}