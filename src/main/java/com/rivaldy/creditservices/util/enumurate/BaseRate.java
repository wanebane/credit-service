package com.rivaldy.creditservices.util.enumurate;

import lombok.Getter;

import java.util.stream.Stream;

import static com.rivaldy.creditservices.util.constant.AppConstant.BASE_RATE_CAR;
import static com.rivaldy.creditservices.util.constant.AppConstant.BASE_RATE_MOTORCYCLE;

public enum BaseRate {
    CAR("mobil", BASE_RATE_CAR),
    MOTORCYCLE("motor", BASE_RATE_MOTORCYCLE);

    @Getter
    private final String vehicleType;

    @Getter
    private final Double vehicleBaseRate;

    BaseRate(String vehicleType, double vehicleBaseRate) {
        this.vehicleType = vehicleType;
        this.vehicleBaseRate = vehicleBaseRate;
    }

    public static double getBaseRate(String vehicleType){
        BaseRate baseRate = Stream.of(BaseRate.values())
                .filter(v -> v.getVehicleType().equalsIgnoreCase(vehicleType.toLowerCase()))
                .findAny().orElse(null);
        if (baseRate == null){
            return 0.0;
        }
        return baseRate.getVehicleBaseRate();
    }

}
