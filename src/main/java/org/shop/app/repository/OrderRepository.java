package org.shop.app.repository;

import org.shop.app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCriteria {

    Optional<Order> findByOrderName(String orderName);

    List<Order> findAllByIsDeletedIsFalse();

    boolean existsByOrderName(String orderName);

}
