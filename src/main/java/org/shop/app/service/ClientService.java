package org.shop.app.service;

import org.shop.app.dto.CreateBuyOrderDto;
import org.shop.app.dto.PayOrderDto;

import java.security.Principal;

public interface ClientService {

    String placeAnOrder(Principal principal, CreateBuyOrderDto createBuyOrderDto);

    String payAnOrder(Principal principal, PayOrderDto payOrderDto);

}
