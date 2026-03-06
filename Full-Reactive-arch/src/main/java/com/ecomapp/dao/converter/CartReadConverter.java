package com.ecomapp.dao.converter;

import com.ecomapp.models.Cart;
import com.ecomapp.models.Product;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class CartReadConverter implements Converter<Row, Cart> {

    @Override
    public Cart convert(Row source) {
        return Cart.builder()
                .id(source.get("id", Integer.class))
                .product((source.get("product", Product.class)))
                .price((source.get("price", Integer.class)))
                .quantity((source.get("quantity", Integer.class)))
                .subtotal((source.get("subtotal", Integer.class)))
                .build();
    }
}
