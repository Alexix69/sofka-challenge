package com.sofka.challenge.client_person.application.dto;

import com.sofka.challenge.common.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordUpdateDTO {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @StrongPassword
    private String newPassword;
}
