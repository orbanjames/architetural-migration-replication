package com.ecomapp.dao.converter;

import com.ecomapp.models.SynodCategory;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class CategoryReadConverter implements Converter<Row, SynodCategory> {

    @Override
    public SynodCategory convert(Row source) {
        return SynodCategory.builder()
                .id(source.get("id", Integer.class))
                .name((source.get("name", String.class)))
                .build();
    }
}
