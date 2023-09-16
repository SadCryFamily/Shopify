package org.shop.app.repository;

import org.shop.app.entity.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByClientName(String clientName);

    boolean existsByClientName(String clientName);

    @EntityGraph("client.roles")
    List<Client> findAll();

}
