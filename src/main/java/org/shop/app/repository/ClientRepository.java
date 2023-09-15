package org.shop.app.repository;

import org.shop.app.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByClientIdAndIsDeletedIsFalse(Long clientId);

    Client findByClientName(String clientName);

    boolean existsByClientNameAndIsDeletedIsFalse(String clientName);

    boolean existsByClientIdAndIsDeletedIsFalse(Long clientId);

}
