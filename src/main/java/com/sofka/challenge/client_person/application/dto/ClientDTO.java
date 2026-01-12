package com.sofka.challenge.client_person.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.challenge.client_person.domain.enums.GenderType;
import com.sofka.challenge.common.validation.OnCreate;
import com.sofka.challenge.common.validation.OnUpdate;
import com.sofka.challenge.common.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {

    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    private GenderType gender;
    @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class})
    private Integer age;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class})
    private String identification;

    @Size(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String address;
    @Size(max = 20, groups = {OnCreate.class, OnUpdate.class})
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank(groups = OnCreate.class)
    @StrongPassword(groups = OnCreate.class)
    private String password;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Boolean status;
}

