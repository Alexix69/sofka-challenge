package com.sofka.challenge.account_transaction.application.usecases;

import com.sofka.challenge.account_transaction.application.dto.AccountDetailsDTO;
import com.sofka.challenge.account_transaction.application.dto.AccountReportDTO;
import com.sofka.challenge.account_transaction.application.dto.TransactionDetailsDTO;
import com.sofka.challenge.account_transaction.domain.AccountEntity;
import com.sofka.challenge.account_transaction.domain.TransactionEntity;
import com.sofka.challenge.account_transaction.infrastructure.persistence.AccountRepository;
import com.sofka.challenge.account_transaction.infrastructure.persistence.TransactionRepository;
import com.sofka.challenge.client_person.domain.ClientEntity;
import com.sofka.challenge.client_person.infrastructure.persistence.ClientRepository;
import com.sofka.challenge.common.exceptions.BadRequestException;
import com.sofka.challenge.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountReportUseCase {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public AccountReportDTO generateAccountReport(Long clientId, LocalDate startDate, LocalDate endDate) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> NotFoundException.of(ClientEntity.class, clientId));

        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        List<AccountEntity> accounts = accountRepository.findByClientId(clientId);
        if (accounts.isEmpty()) {
            return AccountReportDTO.builder()
                    .clientId(client.getId())
                    .clientName(client.getPerson().getName())
                    .accounts(List.of())
                    .build();
        }

        List<TransactionEntity> transactions = transactionRepository.findByClientTransactions(
                clientId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59)
        );

        var transactionsByAccount = transactions.stream()
                .collect(Collectors.groupingBy(tx -> tx.getAccount().getId()));

        List<AccountDetailsDTO> accountDetailsList = accounts.stream()
                .map(account -> buildAccountDetails(account, transactionsByAccount.getOrDefault(account.getId(), List.of()), startDate))
                .collect(Collectors.toList());

        return AccountReportDTO.builder()
                .clientId(client.getId())
                .clientName(client.getPerson().getName())
                .accounts(accountDetailsList)
                .build();
    }


    private AccountDetailsDTO buildAccountDetails(AccountEntity account, List<TransactionEntity> transactions, LocalDate startDate) {
        double balanceBeforeStartDate = transactions.stream()
                .filter(tx -> tx.getTransactionDate().toLocalDate().isBefore(startDate))
                .mapToDouble(TransactionEntity::getAmount)
                .sum();

        double initialBalance = transactionRepository.findByAccountId(account.getId()).stream()
                .filter(tx -> tx.getTransactionDate().toLocalDate().isBefore(startDate))
                .mapToDouble(TransactionEntity::getAmount)
                .sum() + account.getInitialBalance();

        double runningBalance = initialBalance;

        List<TransactionDetailsDTO> transactionDetails = new ArrayList<>();

        for (TransactionEntity transaction : transactions.stream()
                .filter(tx -> !tx.getTransactionDate().toLocalDate().isBefore(startDate))
                .sorted(Comparator.comparing(TransactionEntity::getTransactionDate))
                .toList()) {

            runningBalance += transaction.getAmount();
            transactionDetails.add(new TransactionDetailsDTO(
                    transaction.getTransactionDate(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    runningBalance
            ));
        }

        boolean insufficientBalance = transactionDetails.stream().anyMatch(tx -> tx.getBalanceAfterTransaction() < 0);

        return AccountDetailsDTO.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(initialBalance)
                .currentBalance(runningBalance)
                .insufficientBalance(insufficientBalance)
                .transactions(transactionDetails)
                .build();
    }
}

