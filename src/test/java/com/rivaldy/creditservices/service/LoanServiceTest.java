package com.rivaldy.creditservices.service;

import com.rivaldy.creditservices.exception.BadRequestException;
import com.rivaldy.creditservices.model.dto.InstallmentDto;
import com.rivaldy.creditservices.model.request.LoanRequest;
import com.rivaldy.creditservices.util.ValidationRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @InjectMocks
    private LoanService loanService;
    private LoanRequest validRequest;
    private double tolerance;

    @BeforeEach
    void setUp() {
        loanService = new LoanService();
        tolerance = 0.001;

        validRequest = new LoanRequest();
        validRequest.setVehicleType("Mobil");
        validRequest.setVehicleCondition("Baru");
        validRequest.setVehicleYear(2025);
        validRequest.setTotalLoanAmount(300_000_000.0);
        validRequest.setLoanTenure(4);
        validRequest.setDownPayment(150_000_000.0);
    }

    @Test
    void calculate_ShouldReturnThreeInstallments_ForThreeYearTenure() {
        List<InstallmentDto> result = loanService.calculate(validRequest);
        assertEquals(4, result.size());
    }

    @Test
    void calculate_ShouldHaveCorrectRates() {
        List<InstallmentDto> result = loanService.calculate(validRequest);
        assertEquals(0.08, result.get(0).getRate(), tolerance);
        assertEquals(0.081, result.get(1).getRate(), tolerance);
        assertEquals(0.086, result.get(2).getRate(), tolerance);
    }

    @Test
    void calculate_ShouldThrowException_WhenInputInvalid() {
        LoanRequest invalidRequest = new LoanRequest();
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> loanService.calculate(invalidRequest)
        );

        assertEquals("Error before calculate loan!", exception.getMessage());
        assertFalse(exception.getErrors().isEmpty());
    }
}