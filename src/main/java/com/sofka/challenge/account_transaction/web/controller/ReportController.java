package com.sofka.challenge.account_transaction.web.controller;

import com.sofka.challenge.account_transaction.application.dto.AccountReportDTO;
import com.sofka.challenge.account_transaction.application.usecases.AccountReportUseCase;
import com.sofka.challenge.common.dtos.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/reports")
public class ReportController {

    private final AccountReportUseCase accountReportUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<AccountReportDTO>> generateAccountReport(
            @RequestParam Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK, "Account report generated successfully",
                accountReportUseCase.generateAccountReport(clientId, startDate, endDate)
        ));
    }
}

