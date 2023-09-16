package org.shop.app.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateBuyOrderDto {

    @NotNull(message = "Order name can't be null")
    private String orderName;

}
