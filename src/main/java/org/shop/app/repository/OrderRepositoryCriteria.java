package org.shop.app.repository;

import org.springframework.data.jpa.repository.Modifying;

public interface OrderRepositoryCriteria {

    @Modifying(flushAutomatically = true)
    int setDeleteAllNotPaidOrders();

}
