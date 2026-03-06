package com.ecomapp.dto.request;

import com.ecomapp.models.Cart;
import com.ecomapp.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MakePaymentRequestDTO {

    private User user;
    private Cart cart;
}

