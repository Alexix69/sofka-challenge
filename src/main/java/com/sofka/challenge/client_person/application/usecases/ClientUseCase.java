package com.sofka.challenge.client_person.application.usecases;

import com.sofka.challenge.account_transaction.domain.AccountEntity;
import com.sofka.challenge.account_transaction.infrastructure.persistence.AccountRepository;
import com.sofka.challenge.account_transaction.infrastructure.persistence.TransactionRepository;
import com.sofka.challenge.client_person.application.dto.ClientDTO;
import com.sofka.challenge.client_person.application.mapper.ClientMapper;
import com.sofka.challenge.client_person.application.mapper.PersonMapper;
import com.sofka.challenge.client_person.domain.ClientEntity;
import com.sofka.challenge.client_person.domain.PersonEntity;
import com.sofka.challenge.client_person.infrastructure.persistence.ClientRepository;
import com.sofka.challenge.client_person.infrastructure.persistence.PersonRepository;
import com.sofka.challenge.common.exceptions.BadRequestException;
import com.sofka.challenge.common.exceptions.UniqueConstraintViolationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(clientMapper::toDTO).collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        ClientEntity client = clientRepository.findById(id).orElseThrow();
        return clientMapper.toDTO(client);
    }

    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO) {
        if (personRepository.findByIdentification(clientDTO.getIdentification()).isPresent()) {
            throw new UniqueConstraintViolationException("identification", clientDTO.getIdentification());
        }

        PersonEntity person = personMapper.toEntity(clientDTO);
        person = personRepository.save(person);

        ClientEntity client = ClientEntity.builder().person(person).password(clientDTO.getPassword()).status(clientDTO.getStatus()).build();
        return clientMapper.toDTO(clientRepository.save(client));
    }


    @Transactional
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        ClientEntity client = clientRepository.findById(id).orElseThrow();
        personRepository.findByIdentification(clientDTO.getIdentification()).ifPresent(existingPerson -> {
            if (!existingPerson.getId().equals(client.getPerson().getId())) {
                throw new UniqueConstraintViolationException("identification", clientDTO.getIdentification());
            }
        });

        PersonEntity person = client.getPerson();
        personMapper.updatePersonFromDto(clientDTO, person);
        personRepository.save(person);
        clientMapper.updateClientFromDto(clientDTO, client);
        return clientMapper.toDTO(clientRepository.save(client));

    }


    @Transactional
    public void deleteClient(Long id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        List<AccountEntity> accounts = accountRepository.findByClientId(id);

        boolean hasTransactions = accounts.stream()
                .anyMatch(account -> !transactionRepository.findByAccountId(account.getId()).isEmpty());
        if (hasTransactions) {
            throw new BadRequestException("Cannot delete client with transaction history");
        }

        accountRepository.deleteAll(accounts);
        clientRepository.delete(client);
    }
}
