package com.sofka.challenge.account_transaction.domain;

import com.sofka.challenge.account_transaction.domain.enums.AccountType;
import com.sofka.challenge.client_person.domain.ClientEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
@SQLDelete(sql = "UPDATE account SET status = false WHERE id = ?")
@Where(clause = "status = true")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false, length = 20, columnDefinition = "account_type_enum")
    private AccountType accountType;

    @Column(name = "initial_balance", nullable = false)
    private Double initialBalance;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
}

