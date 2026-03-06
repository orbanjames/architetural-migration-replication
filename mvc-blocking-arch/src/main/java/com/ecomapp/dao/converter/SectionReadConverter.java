package com.ecomapp.dao.converter;

import com.ecomapp.models.SynodCategory;
import com.ecomapp.models.SynodSection;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class SectionReadConverter implements Converter<Row, SynodSection> {

    @Override
    public SynodSection convert(Row source) {
        return SynodSection.builder()
                .id(source.get("id", Integer.class))
                .name(source.get("name", String.class))
                .category(getSynodCategory(source))
                .build();
    }

    private SynodCategory getSynodCategory(Row source) {
        return SynodCategory.builder()
                .id(source.get("category", Integer.class))
                .name(source.get("categoryName", String.class))
                .description(source.get("categoryDescription", String.class))
                .build();
    }
}
