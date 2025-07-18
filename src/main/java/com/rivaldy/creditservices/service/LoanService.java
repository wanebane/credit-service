package com.rivaldy.creditservices.service;

import com.rivaldy.creditservices.exception.BadRequestException;
import com.rivaldy.creditservices.model.dto.InstallmentDto;
import com.rivaldy.creditservices.model.request.LoanRequest;
import com.rivaldy.creditservices.util.ValidationInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.rivaldy.creditservices.util.constant.AppConstant.BASE_RATE_CAR;
import static com.rivaldy.creditservices.util.constant.AppConstant.BASE_RATE_MOTORCYCLE;

@Slf4j
@Service
public class LoanService {

    public List<InstallmentDto> calculate(LoanRequest request){
        List<String> errors = ValidationInput.validationRequest(request);
        if (!errors.isEmpty()){
            throw new BadRequestException("Error before calculate loan!", errors);
        }
        double principal = request.getTotalLoanAmount() - request.getDownPayment();
        List<InstallmentDto> installments = new ArrayList<>();

        double currentRate = 0.0;
        for (int year = 1; year <= request.getLoanTenure(); year++) {
            currentRate = calculateDynamicRate(request.getVehicleType(), year, currentRate);
            double yearlyInstallment = (principal * currentRate) + (principal / request.getLoanTenure());
            installments.add(new InstallmentDto(year, yearlyInstallment / 12, currentRate));
        }

        installments.forEach(System.out::println);
        return installments;
    }

    private double calculateDynamicRate(String vehicleType, int year, double currentRate) {
        double baseRate = vehicleType.equalsIgnoreCase("mobil") ? BASE_RATE_CAR : BASE_RATE_MOTORCYCLE;
        if (year == 1){
            return baseRate;
        }
        double rateYoY = year%2==0 ? 0.001 : 0.005;
        return currentRate + rateYoY;
    }
}
