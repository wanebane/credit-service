package com.rivaldy.creditservices.util;

import com.rivaldy.creditservices.model.request.LoanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ValidationInputTest {

    @Mock
    private ValidationRate validationRate;

    @InjectMocks
    private ValidationInput validationInput;

    private LoanRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new LoanRequest();
        validRequest.setVehicleType("Mobil");
        validRequest.setVehicleCondition("Baru");
        validRequest.setVehicleYear(2025);
        validRequest.setTotalLoanAmount(600_000_000.0);
        validRequest.setLoanTenure(5);
        validRequest.setDownPayment(350_000_000.0);
    }

    @Test
    void validationRequest_ShouldReturnEmptyList_WhenInputValid() {
        when(validationRate.getBaseDownPayment("Baru")).thenReturn(0.35);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertTrue(errors.isEmpty());
    }

    @Test
    void validationRequest_ShouldReturnError_WhenVehicleTypeNull() {
        validRequest.setVehicleType(null);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Vehicle Type"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenVehicleTypeInvalid() {
        validRequest.setVehicleType("Truck");

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("must be 'Mobil' or 'Motor'"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenVehicleConditionNull() {
        validRequest.setVehicleCondition(null);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Vehicle Condition"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenVehicleConditionInvalid() {
        validRequest.setVehicleCondition("Used");

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("must be 'Baru' or 'Bekas'"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenVehicleYearInvalidForNew() {
        validRequest.setVehicleYear(2020);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("should not input < 1 year"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenVehicleYearInvalidNull() {
        validRequest.setVehicleYear(null);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Vehicle Year"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenTotalLoanAmountExceedsMax() {
        validRequest.setTotalLoanAmount(1_050_000_000.0);
        validRequest.setDownPayment(700_000_000.0);

        List<String> errors = validationInput.validationRequest(validRequest);
        errors.forEach(System.out::println);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("must be between 0 and"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenTotalLoanAmountZero() {
        validRequest.setTotalLoanAmount(0.0);
        validRequest.setDownPayment(0.0);

        List<String> errors = validationInput.validationRequest(validRequest);
        errors.forEach(System.out::println);
        assertEquals(2, errors.size());
        assertTrue(errors.get(0).contains("must be between 0 and"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenTotalLoanAmountNull() {
        validRequest.setTotalLoanAmount(null);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Total Loan Amount"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenTenureExceedsMax() {
        validRequest.setLoanTenure(7);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("must be between 1 and 6"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenTenureNull() {
        validRequest.setLoanTenure(null);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Loan Tenure"));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenDownPaymentBelowMinimum() {
        validRequest.setDownPayment(30_000_000.0); // 30% of 100M (min 35%)
        when(validationRate.getBaseDownPayment("Baru")).thenReturn(0.35);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Down Payment should input >="));
    }

    @Test
    void validationRequest_ShouldReturnError_WhenDownPaymentNull() {
        validRequest.setDownPayment(null);

        List<String> errors = validationInput.validationRequest(validRequest);

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("Down Payment"));
    }

    @Test
    void validationRequest_ShouldReturnMultipleErrors_WhenMultipleFieldsInvalid() {
        validRequest.setVehicleType(null);
        validRequest.setVehicleCondition(null);
        validRequest.setTotalLoanAmount(15000000000.0);
        validRequest.setLoanTenure(0);
        validRequest.setDownPayment(0.0);

        List<String> errors = validationInput.validationRequest(validRequest);

        errors.forEach(System.out::println);
        assertEquals(5, errors.size());
    }


    @Test
    void validationRequest_ShouldReturnError_WhenDownPaymentsMoreThanLoanAmount() {
        validRequest.setDownPayment(validRequest.getTotalLoanAmount() + 30_000_000);

        List<String> errors = validationInput.validationRequest(validRequest);

        errors.forEach(System.out::println);
        assertEquals(1, errors.size());
    }

}