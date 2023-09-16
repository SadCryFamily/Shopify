package org.shop.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeleteUnpayedOrdersScheduler {

    @Autowired
    private final OrderRepository orderRepository;

    @Transactional
    @Scheduled(fixedDelayString = "${shop.scheduler-delete-wait}")
    public void deleteNotPayedOrders() {
        int deletedOrders = orderRepository.setDeleteAllNotPaidOrders();
        log.info("DELETED {} not payed orders", deletedOrders);
    }

}
