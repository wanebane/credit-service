package com.rivaldy.creditservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rivaldy.creditservices.model.request.LoanRequest;
import com.rivaldy.creditservices.service.LoanService;
import com.rivaldy.creditservices.util.ValidationRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CLIControllerTest {

    @Mock
    private LoanService loanService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ValidationRate validationRate;

    @InjectMocks
    private CLIController cliController;

    private final String testInputFileContent = "vehicleType=Mobil\n" +
            "vehicleCondition=Baru\n" +
            "vehicleYear=2023\n" +
            "totalLoan=100000000\n" +
            "tenure=3\n" +
            "downPayment=35000000";

    @BeforeEach
    void setUp() throws IOException {
        // Mock ValidationRate responses
        when(validationRate.getBaseDownPayment("baru")).thenReturn(0.35);
        when(validationRate.getBaseDownPayment("bekas")).thenReturn(0.25);
    }

    @Test
    void run_ShouldShowMenu_WhenCliArgumentProvided() {
        assertThrows(NoSuchElementException.class,
                () -> cliController.run("--cli"));
    }

    @Test
    void parseInput_ShouldHandleKeyValueFormat() {
        // Execute
        LoanRequest result = cliController.parseInput(testInputFileContent);

        // Verify
        assertEquals("Mobil", result.getVehicleType());
        assertEquals("Baru", result.getVehicleCondition());
        assertEquals(2023, result.getVehicleYear());
        assertEquals(100000000.0, result.getTotalLoanAmount());
        assertEquals(3, result.getLoanTenure());
        assertEquals(35000000.0, result.getDownPayment());
    }

    @Test
    void parseInput_ShouldHandleJsonFormat() throws Exception {
        // Setup
        String jsonContent = "{\"vehicleType\":\"Motor\",\"vehicleCondition\":\"Bekas\",\"vehicleYear\":2020}";
        LoanRequest expectedRequest = new LoanRequest();
        expectedRequest.setVehicleType("Motor");
        expectedRequest.setVehicleCondition("Bekas");
        expectedRequest.setVehicleYear(2020);

        when(objectMapper.readValue(jsonContent, LoanRequest.class)).thenReturn(expectedRequest);

        // Execute
        LoanRequest result = cliController.parseInput(jsonContent);

        // Verify
        assertEquals("Motor", result.getVehicleType());
        assertEquals("Bekas", result.getVehicleCondition());
        assertEquals(2020, result.getVehicleYear());
    }

    @Test
    void askVehicleType_ShouldReturnValidType() {
        // Simulate user input
        String input = "Mobil";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cliController = new CLIController(loanService, objectMapper/*, new Scanner(System.in)*/);

        // Execute
        String result = cliController.askVehicleType();

        // Verify
        assertEquals("mobil", result);
    }

    @Test
    void askDownPayment_ShouldReturnValidAmount() {
        // Simulate user input
        String input = "35000000\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cliController = new CLIController(loanService, objectMapper/*, new Scanner(System.in)*/);

        // Execute
        double result = cliController.askDownPayment("baru", 100000000.0);

        // Verify
        assertEquals(35000000.0, result);
    }

    @Test
    void askVehicleYear_ShouldReturnValidYearForNewVehicle() {
        // Simulate user input
        String input = "2025\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cliController = new CLIController(loanService, objectMapper/*, new Scanner(System.in)*/);

        // Execute
        int result = cliController.askVehicleYear("baru");

        // Verify
        assertEquals(2025, result);
    }

    @Test
    void askVehicleYear_ShouldReturnValidYearForUsedVehicle() {
        // Simulate user input
        String input = "2020\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cliController = new CLIController(loanService, objectMapper/*, new Scanner(System.in)*/);

        // Execute
        int result = cliController.askVehicleYear("bekas");

        // Verify
        assertEquals(2020, result);
    }
}