package org.shop.app.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ViewOrderDto {

    @NotNull(message = "Order name can't be null")
    private String orderName;

    @NotNull(message = "Price can't be null")
    @Min(value = 10, message = "Price must be greater than 10$")
    private BigDecimal orderUnitPrice;

    @NotNull(message = "Quantity can't be null")
    @Min(value = 1, message = "Quantity must be greater than 1 unit")
    private Integer orderUnitQuantity;

}
