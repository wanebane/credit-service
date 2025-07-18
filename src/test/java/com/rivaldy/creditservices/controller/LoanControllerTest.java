package com.rivaldy.creditservices.controller;

import com.rivaldy.creditservices.model.dto.InstallmentDto;
import com.rivaldy.creditservices.model.request.LoanRequest;
import com.rivaldy.creditservices.model.response.SuccessResponse;
import com.rivaldy.creditservices.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private LoanRequest validRequest;
    private List<InstallmentDto> mockInstallments;

    @BeforeEach
    void setUp() {
        // Setup valid request
        validRequest = new LoanRequest();
        validRequest.setVehicleType("Mobil");
        validRequest.setVehicleCondition("Baru");
        validRequest.setVehicleYear(2025);
        validRequest.setTotalLoanAmount(100_000_000.0);
        validRequest.setLoanTenure(3);
        validRequest.setDownPayment(35_000_000.0);

        // Setup mock installments
        mockInstallments = Arrays.asList(
                new InstallmentDto(1, 2_500_000.0, 0.08),
                new InstallmentDto(2, 2_520_000.0, 0.081),
                new InstallmentDto(3, 2_550_000.0, 0.086)
        );
    }

    @Test
    void calculate_ShouldReturnSuccessResponse_WhenInputValid() {
        // Mock service
        when(loanService.calculate(validRequest)).thenReturn(mockInstallments);

        // Execute
        ResponseEntity<SuccessResponse> response = loanController.calculate(validRequest);

        // Verify
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success to calculate data", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());

        // Verify service called once
        verify(loanService, times(1)).calculate(validRequest);
    }

    @Test
    void calculate_ShouldReturnCorrectInstallmentStrings() {
        // Mock service
        when(loanService.calculate(validRequest)).thenReturn(mockInstallments);

        // Execute
        ResponseEntity<SuccessResponse> response = loanController.calculate(validRequest);
        List<String> resultStrings = new ArrayList<>();
        if (Objects.nonNull(response.getBody())){
            Object obj = response.getBody().getData();
            if(obj instanceof Collection<?>){
                resultStrings = new ArrayList<>((Collection<String>) obj);
            }
        }

        // Verify string format
        assertEquals(3, resultStrings.size());
        assertTrue(resultStrings.get(0).contains("Tahun 1"));
        assertTrue(resultStrings.get(1).contains("Rp. 2.520.000,00/month"));
        assertTrue(resultStrings.get(0).contains("Suku Bunga: 8%"));
    }

    @Test
    void calculate_ShouldCallServiceWithCorrectParameters() {
        // Mock service
        when(loanService.calculate(validRequest)).thenReturn(mockInstallments);

        // Execute
        loanController.calculate(validRequest);

        // Verify service called with correct request
        verify(loanService).calculate(argThat(req ->
                req.getVehicleType().equals("Mobil") &&
                        req.getVehicleCondition().equals("Baru") &&
                        req.getTotalLoanAmount() == 100_000_000.0
        ));
    }
}