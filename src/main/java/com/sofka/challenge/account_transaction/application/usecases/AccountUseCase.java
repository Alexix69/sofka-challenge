package com.sofka.challenge.account_transaction.application.usecases;

import com.sofka.challenge.account_transaction.application.dto.AccountDTO;
import com.sofka.challenge.account_transaction.application.mapper.AccountMapper;
import com.sofka.challenge.account_transaction.domain.AccountEntity;
import com.sofka.challenge.account_transaction.infrastructure.persistence.AccountRepository;
import com.sofka.challenge.account_transaction.infrastructure.persistence.TransactionRepository;
import com.sofka.challenge.client_person.domain.ClientEntity;
import com.sofka.challenge.client_person.infrastructure.persistence.ClientRepository;
import com.sofka.challenge.common.exceptions.BadRequestException;
import com.sofka.challenge.common.exceptions.NotFoundException;
import com.sofka.challenge.common.exceptions.UniqueConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AccountDTO getAccountById(Long id) {
        AccountEntity account = accountRepository.findById(id).orElseThrow();
        return accountMapper.toDTO(account);
    }

    public List<AccountDTO> getAccountsByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw NotFoundException.of("Client", clientId);
        }
        return accountRepository.findByClientId(clientId).stream()
                .map(accountMapper::toDTO)
                .toList();
    }

    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        ClientEntity client = clientRepository.findById(accountDTO.getClientId())
                .orElseThrow(() -> NotFoundException.of(ClientEntity.class, accountDTO.getClientId()));

        if (accountRepository.existsByAccountNumber(accountDTO.getAccountNumber())) {
            throw new UniqueConstraintViolationException("accountNumber", accountDTO.getAccountNumber());
        }

        AccountEntity account = accountMapper.toEntity(accountDTO);
        account.setClient(client);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(AccountEntity.class, id));

        ClientEntity client = clientRepository.findById(accountDTO.getClientId())
                .orElseThrow(() -> NotFoundException.of(ClientEntity.class, accountDTO.getClientId()));

        if (!account.getClient().getId().equals(client.getId())) {
            throw new BadRequestException("Account cannot be transferred to another client");
        }

        accountRepository.findByAccountNumber(accountDTO.getAccountNumber()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new UniqueConstraintViolationException("accountNumber", accountDTO.getAccountNumber());
            }
        });


        accountMapper.updateAccountFromDto(accountDTO, account);
        account.setClient(client);
        return accountMapper.toDTO(accountRepository.save(account));
    }

    @Transactional
    public void deleteAccount(Long id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(AccountEntity.class, id));

        boolean hasTransactions = !transactionRepository.findByAccountId(account.getId()).isEmpty();
        if (hasTransactions) {
            throw new BadRequestException("Cannot delete account with transaction history");
        }

        accountRepository.delete(account);
    }

}

