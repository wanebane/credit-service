package com.rivaldy.creditservices.controller;

import com.rivaldy.creditservices.model.dto.InstallmentDto;
import com.rivaldy.creditservices.model.request.LoanRequest;
import com.rivaldy.creditservices.model.response.SuccessResponse;
import com.rivaldy.creditservices.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan")
@AllArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/calculate")
    public ResponseEntity<SuccessResponse> calculate(@RequestBody LoanRequest request){
        List<String> installmentResult = loanService.calculate(request).stream().map(InstallmentDto::toString).toList();

        SuccessResponse response = SuccessResponse.builder()
                .message("Success to calculate data")
                .data(installmentResult)
                .build();

        return ResponseEntity.ok(response);
    }
}
