package com.ecomapp.services;

import com.ecomapp.models.Payment;
import reactor.core.publisher.Mono;

public interface PaymentService extends GenericService<Payment> {


    Mono<Payment> getByStatus(String paymentStatus);

}
