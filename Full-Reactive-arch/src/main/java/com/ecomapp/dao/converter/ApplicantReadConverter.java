package com.ecomapp.dao.converter;

import com.ecomapp.models.Applicant;
import com.ecomapp.models.User;
import com.ecomapp.models.enums.ApplicantStatusEnum;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class ApplicantReadConverter implements Converter<Row, Applicant> {

    @Override
    public Applicant convert(Row source) {
        return Applicant.builder()
                .id(source.get("id", Integer.class))
                .catalogue(ReviewReadConverter.getCatalogue(source))
                .synodMember(User.builder().id(source.get("synodMember", Integer.class)).build())
                .status(ApplicantStatusEnum.valueOf(source.get("status", String.class)))
                .build();
    }
}
