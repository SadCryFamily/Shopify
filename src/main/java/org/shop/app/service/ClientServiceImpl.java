package org.shop.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.app.dto.CreateBuyOrderDto;
import org.shop.app.dto.PayOrderDto;
import org.shop.app.exception.*;
import org.shop.app.entity.Client;
import org.shop.app.entity.Order;
import org.shop.app.enums.ExceptionMessage;
import org.shop.app.repository.ClientRepository;
import org.shop.app.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public String placeAnOrder(Principal principal, CreateBuyOrderDto createBuyOrderDto) {

        String username = principal.getName();

        if (!clientRepository.existsByClientName(username)) {
            log.error("ERROR PLACE ORDER BY CLIENT: {}. Reason: {}", username, ClientUnableToFindException.class);
            throw new ClientUnableToFindException(ExceptionMessage.CLIENT_NOT_FOUND.getExceptionMessage());
        }

        Optional<Order> optionalOrder = orderRepository.findByOrderName(createBuyOrderDto.getOrderName());

        if (optionalOrder.isEmpty()) {
            log.error("ERROR PLACE ORDER BY CLIENT: {}. Reason: {}", username, OrderNullException.class);
            throw new OrderNullException(ExceptionMessage.ORDER_NULL.getExceptionMessage());
        }

        Order checkedOrder = optionalOrder.get();

        Client client = clientRepository.findByClientName(username);

        if (checkedOrder.getClient() != null) {
            log.error("ERROR PLACE ORDER BY CLIENT: {}. Reason: {}", username, OrderClientAlreadyPresentException.class);
            throw new OrderClientAlreadyPresentException(ExceptionMessage.ORDER_CLIENT_ALREADY_PRESENT.getExceptionMessage());
        }

        client.addOrder(checkedOrder);

        log.info("ORDER with NAME: {} PLACED BY CLIENT: {}", checkedOrder.getOrderName(), client.getClientName());
        return String.format("Order [%s] placed successful", checkedOrder.getOrderName());

    }

    @Override
    @Transactional
    public String payAnOrder(Principal principal, PayOrderDto payOrderDto) {

        String username = principal.getName();

        if (!clientRepository.existsByClientName(username)) {
            log.error("ERROR PAY ORDER BY CLIENT: {}. Reason: {}", username, ClientUnableToFindException.class);
            throw new ClientUnableToFindException(ExceptionMessage.CLIENT_NOT_FOUND.getExceptionMessage());
        }

        Optional<Order> optionalOrder = orderRepository.findByOrderName(payOrderDto.getOrderName());

        if (optionalOrder.isEmpty()) {
            log.error("ERROR PAY ORDER BY CLIENT: {}. Reason: {}", username, OrderNullException.class);
            throw new OrderNullException(ExceptionMessage.ORDER_NULL.getExceptionMessage());
        }

        Order checkedOrder = optionalOrder.get();
        String ownerUsername = checkedOrder.getClient().getClientName();

        if (checkedOrder.getClient() != null && !Objects.equals(ownerUsername, username)) {
            log.error("ERROR PAY ORDER BY CLIENT: {}. Reason: {}", username, NotMyOrderToPayException.class);
            throw new NotMyOrderToPayException(ExceptionMessage.ORDER_NOT_MY.getExceptionMessage());
        }

        checkedOrder.setPayed(true);

        log.info("PAY ORDER BY CLIENT: {} WITH ORDER NAME: {}", username, checkedOrder.getOrderName());
        return String.format("Order [%s] successfully payed", payOrderDto.getOrderName());

    }
}
