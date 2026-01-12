package com.sofka.challenge.client_person.web.controller;

import com.sofka.challenge.client_person.application.dto.ClientDTO;
import com.sofka.challenge.client_person.application.dto.PasswordUpdateDTO;
import com.sofka.challenge.client_person.application.usecases.ClientUseCase;
import com.sofka.challenge.common.dtos.ApiResponse;
import com.sofka.challenge.common.validation.OnCreate;
import com.sofka.challenge.common.validation.OnUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/clients")
public class ClientController {

    private final ClientUseCase clientUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientDTO>>> getAllClients() {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Clients retrieved successfully", clientUseCase.getAllClients()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDTO>> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Client retrieved successfully", clientUseCase.getClientById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClientDTO>> createClient(@Validated(OnCreate.class) @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.created(URI.create("/v1/clients")).body(ApiResponse.success(HttpStatus.CREATED, "Client created successfully", clientUseCase.createClient(clientDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDTO>> updateClient(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Client updated successfully", clientUseCase.updateClient(id, clientDTO)));

    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        clientUseCase.updatePassword(id, passwordUpdateDTO);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "Password updated successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Long id) {
        clientUseCase.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(HttpStatus.NO_CONTENT, "Client deleted successfully", null));
    }
}

