package com.rivaldy.creditservices.util.constant;

import java.util.List;

public class AppConstant {

    public static final double BASE_RATE_CAR = 0.08;
    public static final double BASE_RATE_MOTORCYCLE = 0.09;

    public static final double BASE_CON_NEW_RATE = 0.35;
    public static final double BASE_CON_OLD_RATE = 0.25;

    public static final int MAX_TENURE = 6;
    public static final double MAX_LOAN_AMOUNT = 1_000_000_000;

    public static final List<String> VEHICLE_TYPES = List.of("mobil", "motor");
    public static final List<String> VEHICLE_CONDITION = List.of("baru", "bekas");

    public static final String NULL_MSG = "%s cannot be empty or null";
}
