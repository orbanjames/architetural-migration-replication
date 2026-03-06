package com.ecomapp.dto.request;

import com.ecomapp.models.Applicant;
import com.ecomapp.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDTO {

    private User registrar;
    private Applicant application;
}
