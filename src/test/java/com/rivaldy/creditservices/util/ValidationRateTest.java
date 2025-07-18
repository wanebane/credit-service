package com.rivaldy.creditservices.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidationRateTest {

    private final ValidationRate validationRate = new ValidationRate();

    @Test
    void getBaseRate_ShouldReturnCorrectRateForMobil() {
        assertEquals(0.08, validationRate.getBaseRate("Mobil"));
        assertEquals(0.08, validationRate.getBaseRate("mobil"));
    }

    @Test
    void getBaseRate_ShouldReturnCorrectRateForMotor() {
        assertEquals(0.09, validationRate.getBaseRate("Motor"));
        assertEquals(0.09, validationRate.getBaseRate("motor"));
    }

    @Test
    void getBaseRate_ShouldReturnZeroForInvalidType() {
        assertEquals(0.0, validationRate.getBaseRate("Truck"));
        assertEquals(0.0, validationRate.getBaseRate(null));
    }

    @Test
    void getBaseDownPayment_ShouldReturnCorrectRateForNew() {
        assertEquals(0.35, validationRate.getBaseDownPayment("Baru"));
        assertEquals(0.35, validationRate.getBaseDownPayment("baru"));
    }

    @Test
    void getBaseDownPayment_ShouldReturnCorrectRateForOld() {
        assertEquals(0.25, validationRate.getBaseDownPayment("Lama"));
        assertEquals(0.25, validationRate.getBaseDownPayment("lama"));
    }

    @Test
    void getBaseDownPayment_ShouldReturnZeroForInvalidCondition() {
        assertEquals(0.0, validationRate.getBaseDownPayment("Used"));
        assertEquals(0.0, validationRate.getBaseDownPayment(null));
    }

    @Test
    void calculateDynamicRate_ShouldReturnBaseRateForYear1() {
        assertEquals(0.08, validationRate.calculateDynamicRate("Mobil", 1, 0.0));
    }

    @Test
    void calculateDynamicRate_ShouldAdd001ForEvenYear() {
        assertEquals(0.081, validationRate.calculateDynamicRate("Mobil", 2, 0.08));
    }

    @Test
    void calculateDynamicRate_ShouldAdd005ForOddYear() {
        assertEquals(0.085, validationRate.calculateDynamicRate("Mobil", 3, 0.08));
    }
}