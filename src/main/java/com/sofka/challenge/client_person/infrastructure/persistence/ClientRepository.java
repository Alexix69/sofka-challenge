package com.sofka.challenge.client_person.infrastructure.persistence;

import com.sofka.challenge.client_person.domain.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

}
