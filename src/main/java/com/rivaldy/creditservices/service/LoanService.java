package com.rivaldy.creditservices.service;

import com.rivaldy.creditservices.exception.BadRequestException;
import com.rivaldy.creditservices.model.dto.InstallmentDto;
import com.rivaldy.creditservices.model.request.LoanRequest;
import com.rivaldy.creditservices.util.ValidationInput;
import com.rivaldy.creditservices.util.ValidationRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LoanService {

    private final ValidationInput validationInput = new ValidationInput();
    private final ValidationRate validationRate = new ValidationRate();

    public List<InstallmentDto> calculate(LoanRequest request){
        List<String> errors = validationInput.validationRequest(request);
        if (!errors.isEmpty()){
            throw new BadRequestException("Error before calculate loan!", errors);
        }
        double principal = request.getTotalLoanAmount() - request.getDownPayment();
        List<InstallmentDto> installments = new ArrayList<>();

        double currentRate = 0.0;
        for (int year = 1; year <= request.getLoanTenure(); year++) {
            currentRate = validationRate.calculateDynamicRate(request.getVehicleType(), year, currentRate);
            double yearlyInstallment = (principal * currentRate) + (principal / request.getLoanTenure());
            installments.add(new InstallmentDto(year, yearlyInstallment / 12, currentRate));
        }

        installments.forEach(System.out::println);
        return installments;
    }
}
