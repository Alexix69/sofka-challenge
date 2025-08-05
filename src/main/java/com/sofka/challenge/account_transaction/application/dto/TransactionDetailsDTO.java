package com.sofka.challenge.account_transaction.application.dto;

import com.sofka.challenge.account_transaction.domain.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetailsDTO {
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private Double amount;
    private Double balanceAfterTransaction;
}
