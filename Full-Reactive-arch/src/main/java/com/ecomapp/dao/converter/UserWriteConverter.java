package com.ecomapp.dao.converter;


import com.ecomapp.dao.converter.helper.UserConverterHelper;
import com.ecomapp.models.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;


@WritingConverter
public class UserWriteConverter implements Converter<User, OutboundRow> {

    @Override
    public OutboundRow convert(User user) {
        return UserConverterHelper.getWriteRow(user);
    }
}

