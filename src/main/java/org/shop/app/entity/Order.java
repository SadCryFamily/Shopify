package org.shop.app.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders",
        uniqueConstraints = @UniqueConstraint(name = "uk_order_name", columnNames = "order_name"))
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

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

}
