package com.rivaldy.creditservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rivaldy.creditservices.model.dto.InstallmentDto;
import com.rivaldy.creditservices.model.request.LoanRequest;
import com.rivaldy.creditservices.service.LoanService;
import com.rivaldy.creditservices.util.enumurate.DownPaymentRate;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import static com.rivaldy.creditservices.util.constant.AppConstant.VEHICLE_CONDITION;
import static com.rivaldy.creditservices.util.constant.AppConstant.VEHICLE_TYPES;

@Component
@AllArgsConstructor
public class CLIController implements CommandLineRunner {

    private final LoanService calculatorService;

    private final ObjectMapper objectMapper;

    private final Scanner scanner = new Scanner(System.in);
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    @Override
    public void run(String... args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--cli")) {
            showModeSelectionMenu();
        }
    }

    private void askToRepeatOrExit() {
        System.out.println("\n=== Pilihan ===");
        System.out.println("1. Ulang Program");
        System.out.println("2. Keluar");
        System.out.print("Pilihan Anda (1/2): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> run();
            case 2 -> {
                System.out.println("Terima kasih, program selesai!");
                System.exit(0);
            }
            default -> System.out.println("Pilihan tidak valid!");
        }
    }

    private void showModeSelectionMenu() {
        System.out.println("=== Pilih Mode ===");
        System.out.println("1. Interactive Mode");
        System.out.println("2. File Input Mode");
        System.out.println("3. Keluar");
        System.out.print("Pilihan Anda (1/2/3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1 -> interactiveMode();
            case 2 -> {
                System.out.print("Masukkan path file input (contoh: input.txt): ");
                String filePath = scanner.nextLine();
                processFileInput(filePath);
            }
            case 3 -> {
                System.out.println("Terima kasih!");
                System.exit(0);
            }
            default -> System.out.println("Pilihan tidak valid!");
        }
    }

    private void processFileInput(String filePath) {
        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            LoanRequest request = parseInput(content);

            List<InstallmentDto> installments = calculatorService.calculate(request);
            askToRepeatOrExit();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }

    private LoanRequest parseInput(String content) {
        try {
            if (content.trim().startsWith("{")) {
                return objectMapper.readValue(content, LoanRequest.class);
            }
            Map<String, String> params = Arrays.stream(content.split("\n"))
                    .map(line -> line.split("="))
                    .collect(Collectors.toMap(
                            arr -> arr[0].trim(),
                            arr -> arr.length > 1 ? arr[1].trim() : ""
                    ));

            return new LoanRequest(
                    params.get("vehicleType"),
                    params.get("vehicleCondition"),
                    Integer.parseInt(params.get("vehicleYear")),
                    Double.parseDouble(params.get("totalLoan")),
                    Integer.parseInt(params.get("tenure")),
                    Double.parseDouble(params.get("downPayment"))
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file format", e);
        }
    }

    private void interactiveMode() {
        System.out.println("=== Vehicle Loan Calculator ===");

        LoanRequest request = new LoanRequest();
        request.setVehicleType(askVehicleType());
        request.setVehicleCondition(askVehicleCondition());
        request.setVehicleYear(askVehicleYear(request.getVehicleCondition()));
        request.setTotalLoanAmount(askTotalLoan());
        request.setLoanTenure(askTenure());
        request.setDownPayment(askDownPayment(
                request.getVehicleCondition(),
                request.getTotalLoanAmount()
        ));

        try {
            List<InstallmentDto> installments = calculatorService.calculate(request);
            askToRepeatOrExit();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private String askVehicleType() {
        while (true) {
            System.out.print("Tipe Kendaraan [Mobil/Motor]: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (VEHICLE_TYPES.contains(input)){
                return input;
            }
            System.out.println("Input Salah! Silahkan input 'Mobil' or 'Motor'");
        }
    }

    private String askVehicleCondition() {
        while (true) {
            System.out.print("Condition [Baru/Bekas]: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (VEHICLE_CONDITION.contains(input)){
                return input;
            }
            System.out.println("Input Salah! Silahkan input 'Baru' or 'Bekas'");
        }
    }

    private int askVehicleYear(String condition) {
        int currentYear = Year.now().getValue();
        int minYear = condition.equalsIgnoreCase("baru") ? currentYear - 1 : 1900;

        while (true) {
            System.out.printf("Vehicle Year (min %d): ", minYear);
            try {
                int year = Integer.parseInt(scanner.nextLine());
                if (year >= minYear && year <= currentYear) {
                    return year;
                }
                System.out.printf("Tahun harus diantara %d dan %d\n", minYear, currentYear);
            } catch (NumberFormatException e) {
                System.out.println("Silahkan input 4 digit angka (tahun)");
            }
        }
    }

    private double askTotalLoan() {
        while (true) {
            System.out.print("Total Angka Pinjaman: ");
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount > 0) {
                    return amount;
                }
                System.out.println("Amount must be between 1 and 1.000.000.000");
            } catch (NumberFormatException e) {
                System.out.println("Silahkan input angka yang benar");
            }
        }
    }

    private int askTenure() {
        while (true) {
            System.out.print("Tenor Pinjaman (1-6 tahun): ");
            try {
                int tenure = Integer.parseInt(scanner.nextLine());
                if (tenure >= 1 && tenure <= 6) {
                    return tenure;
                }
                System.out.println("Tenor Pinjaman harus diantara 1 dan 6 tahun");
            } catch (NumberFormatException e) {
                System.out.println("Silahkan input angka yang benar");
            }
        }
    }

    private double askDownPayment(String condition, double totalLoan) {
        double minPercentage = DownPaymentRate.getBaseDownPayment(condition);
        double minAmount = totalLoan * minPercentage;

        while (true) {
            System.out.printf("Down Payment (min %s): ", currencyFormatter.format(minAmount));
            try {
                double dp = Double.parseDouble(scanner.nextLine());
                if (dp >= minAmount && dp < totalLoan) {
                    return dp;
                }
                System.out.printf("Down Payment must be between %s and %s\n",
                        currencyFormatter.format(minAmount),
                        currencyFormatter.format(totalLoan - 1)
                );
            } catch (NumberFormatException e) {
                System.out.println("Silahkan input angka yang benar");
            }
        }
    }
}
