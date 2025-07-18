package com.rivaldy.creditservices.util.enumurate;

import lombok.Getter;

import static com.rivaldy.creditservices.util.constant.AppConstant.*;

public enum DownPaymentRate {

    NEW("baru", BASE_CON_NEW_RATE),
    OLD("lama", BASE_CON_OLD_RATE);

    @Getter
    private final String vehicleCondition;

    @Getter
    private final Double vehicleBaseDP;

    DownPaymentRate(String vehicleCondition, double vehicleBaseDP) {
        this.vehicleCondition = vehicleCondition;
        this.vehicleBaseDP = vehicleBaseDP;
    }
}
