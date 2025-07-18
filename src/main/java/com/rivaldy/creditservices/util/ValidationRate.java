package com.rivaldy.creditservices.util;

import com.rivaldy.creditservices.util.enumurate.BaseRate;
import com.rivaldy.creditservices.util.enumurate.DownPaymentRate;
import io.micrometer.common.util.StringUtils;

import java.util.stream.Stream;

public class ValidationRate {

    public double calculateDynamicRate(String vehicleType, int year, double currentRate) {
        double baseRate = getBaseRate(vehicleType);
        if (year == 1){
            return baseRate;
        }
        double rateYoY = year%2==0 ? 0.001 : 0.005;
        return currentRate + rateYoY;
    }

    public double getBaseRate(String vehicleType){
        if (StringUtils.isEmpty(vehicleType)){
            return 0.0;
        }
        BaseRate baseRate = Stream.of(BaseRate.values())
                .filter(v -> v.getVehicleType().equalsIgnoreCase(vehicleType.toLowerCase()))
                .findAny().orElse(null);
        if (baseRate == null){
            return 0.0;
        }
        return baseRate.getVehicleBaseRate();
    }

    public double getBaseDownPayment(String vehicleCondition){
        if (StringUtils.isEmpty(vehicleCondition)){
            return 0.0;
        }
        DownPaymentRate downPaymentRate = Stream.of(DownPaymentRate.values())
                .filter(v -> v.getVehicleCondition().equalsIgnoreCase(vehicleCondition.toLowerCase()))
                .findAny().orElse(null);
        if (downPaymentRate == null){
            return 0.0;
        }
        return downPaymentRate.getVehicleBaseDP();
    }
}
