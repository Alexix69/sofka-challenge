package com.sofka.challenge.account_transaction.application.usecases;

import com.sofka.challenge.account_transaction.application.dto.TransactionDTO;
import com.sofka.challenge.account_transaction.application.mapper.TransactionMapper;
import com.sofka.challenge.account_transaction.domain.AccountEntity;
import com.sofka.challenge.account_transaction.domain.TransactionEntity;
import com.sofka.challenge.account_transaction.domain.enums.TransactionType;
import com.sofka.challenge.account_transaction.infrastructure.persistence.AccountRepository;
import com.sofka.challenge.account_transaction.infrastructure.persistence.TransactionRepository;
import com.sofka.challenge.common.exceptions.BadRequestException;
import com.sofka.challenge.common.exceptions.InsufficientBalanceException;
import com.sofka.challenge.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    public TransactionDTO getTransactionById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(TransactionEntity.class, id));
        return transactionMapper.toDTO(transaction);
    }

    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> NotFoundException.of(AccountEntity.class, accountId));

        return transactionRepository.findByAccountId(accountId).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }


    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        AccountEntity account = accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> NotFoundException.of(AccountEntity.class, transactionDTO.getAccountId()));

        if (!transactionDTO.getTransactionType().isValidAmount(transactionDTO.getAmount())) {
            throw new BadRequestException("Invalid transaction amount for type: " + transactionDTO.getTransactionType());
        }

        double currentBalance = transactionRepository.findByAccountId(account.getId()).stream()
                .mapToDouble(TransactionEntity::getAmount)
                .sum() + account.getInitialBalance();

        if (transactionDTO.getTransactionType() == TransactionType.WITHDRAWAL && (currentBalance + transactionDTO.getAmount() < 0)) {
            throw new InsufficientBalanceException("Insufficient funds to complete this transaction");
        }

        TransactionEntity transaction = transactionMapper.toEntity(transactionDTO);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setBalance(currentBalance + transactionDTO.getAmount()); 
        transaction.setAccount(account);

        transactionRepository.save(transaction);
        return transactionMapper.toDTO(transaction);
    }


}


