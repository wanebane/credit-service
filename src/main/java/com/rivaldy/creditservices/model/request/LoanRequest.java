package com.rivaldy.creditservices.model.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanRequest {

    private String vehicleType;
    private String vehicleCondition;
    private Integer vehicleYear;
    private Double totalLoanAmount;
    private Integer loanTenure;
    private Double downPayment;
}
