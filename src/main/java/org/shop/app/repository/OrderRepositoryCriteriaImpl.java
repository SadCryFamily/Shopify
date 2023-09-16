package org.shop.app.repository;

import lombok.extern.slf4j.Slf4j;
import org.shop.app.entity.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

@Repository
public class OrderRepositoryCriteriaImpl implements OrderRepositoryCriteria{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public int setDeleteAllNotPaidOrders() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Order> updateCriteria = criteriaBuilder.createCriteriaUpdate(Order.class);

        Root<Order> orderRoot = updateCriteria.from(Order.class);

        updateCriteria.set(orderRoot.get("isDeleted"), true)
                .where(
                        criteriaBuilder.and(
                                criteriaBuilder.isNotNull(orderRoot.get("client")),
                                criteriaBuilder.equal(orderRoot.get("isPayed"), false)
                        )
                );

        return entityManager.createQuery(updateCriteria).executeUpdate();
    }
}
