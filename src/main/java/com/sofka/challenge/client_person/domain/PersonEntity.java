package com.sofka.challenge.client_person.domain;

import com.sofka.challenge.client_person.domain.enums.GenderType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(length = 10, columnDefinition = "gender_type")
    private GenderType gender;

    private Integer age;

    @Column(nullable = false, unique = true, length = 50)
    private String identification;

    @Column()
    private String address;

    @Column(length = 20)
    private String phone;

}
