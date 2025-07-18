package com.rivaldy.creditservices.util;

import com.rivaldy.creditservices.model.request.LoanRequest;
import io.micrometer.common.util.StringUtils;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static com.rivaldy.creditservices.util.constant.AppConstant.*;

public class ValidationInput {

    private final ValidationRate validationRate = new ValidationRate();

    public List<String> validationRequest(LoanRequest request){
        List<String> errors = new ArrayList<>();
        String reqTypes = request.getVehicleType();
        boolean isEmptyTypes = StringUtils.isEmpty(reqTypes);
        if (isEmptyTypes){
            errors.add(String.format(NULL_MSG, "Vehicle Type"));
        }
        if (!isEmptyTypes && !VEHICLE_TYPES.contains(reqTypes.toLowerCase())){
            errors.add("Vehicle Type input must be 'Mobil' or 'Motor'");
        }

        String reqCondition = request.getVehicleCondition();
        boolean isEmptyCondition = StringUtils.isEmpty(reqCondition);
        if (isEmptyCondition){
            errors.add(String.format(NULL_MSG, "Vehicle Condition"));
        }
        if (!isEmptyCondition && !VEHICLE_CONDITION.contains(request.getVehicleCondition().toLowerCase())){
            errors.add("Vehicle Condition input must be 'Baru' or 'Bekas'");
        }

        int currentYear = Year.now().getValue();
        boolean isConditionNew = !isEmptyCondition && request.getVehicleCondition().equalsIgnoreCase("baru");
        boolean isNullYear = request.getVehicleYear() == null;
        if (isNullYear){
            errors.add(String.format(NULL_MSG, "Vehicle Year"));
        }
        if (!isNullYear && isConditionNew && request.getVehicleYear() < currentYear - 1){
            errors.add("Vehicle Year should not input < 1 year with Vehicle Condition 'Baru'");
        }

        boolean isNullTotalLoan = request.getTotalLoanAmount() == null;
        if (isNullTotalLoan){
            errors.add(String.format(NULL_MSG, "Total Loan Amount"));
        }
        if (!isNullTotalLoan && (request.getTotalLoanAmount() == 0 || request.getTotalLoanAmount() > MAX_LOAN_AMOUNT)){
            errors.add("Total Loan Amount must be between 0 and "+FormatData.currencyFormat(MAX_LOAN_AMOUNT));
        }

        boolean isNullTenure = request.getLoanTenure() == null;
        if (isNullTenure){
            errors.add(String.format(NULL_MSG, "Loan Tenure"));
        }

        if (!isNullTenure && (request.getLoanTenure() < 1 || request.getLoanTenure() > MAX_TENURE)){
            errors.add("Loan Tenure must be between 1 and "+MAX_TENURE);
        }

        boolean isNullDP = request.getDownPayment() == null;
        if (isNullDP){
            errors.add(String.format(NULL_MSG, "Down Payment"));
        }
        if (!isNullDP && request.getDownPayment() == 0){
            errors.add("Down Payment cannot input 0");
        }
        if (!isNullDP && !isNullTotalLoan && !isEmptyCondition && request.getDownPayment() > 0){
            double principal = request.getTotalLoanAmount() - request.getDownPayment();
            if (principal < 0){
                errors.add("Down Payment must be lower than Total Loan Amount");
            }
            double minDPPercentage = validationRate.getBaseDownPayment(request.getVehicleCondition());
            double minDP = request.getTotalLoanAmount() * minDPPercentage;
            if (request.getDownPayment() < minDP){
                errors.add("Down Payment should input >= "+FormatData.currencyFormat(minDP));
            }
        }
        return errors;
    }
}
