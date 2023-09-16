package org.shop.app.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "order_unit_price")
    private BigDecimal orderUnitPrice;

    @Column(name = "order_unit_quantity")
    private Integer orderUnitQuantity;

    @Column(name = "is_payed")
    private boolean isPayed;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
