package com.sofka.challenge.account_transaction.infrastructure.persistence;

import com.sofka.challenge.account_transaction.domain.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByClientId(Long clientId);

    boolean existsByAccountNumber(String accountNumber);

    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
