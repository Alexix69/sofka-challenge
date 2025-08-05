package com.sofka.challenge.client_person.domain;

import jakarta.persistence.*;
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

    @Column(length = 10)
    private String gender;

    private Integer age;

    @Column(nullable = false, unique = true, length = 50)
    private String identification;

    @Column()
    private String address;

    @Column(length = 20)
    private String phone;

}
