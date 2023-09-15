package org.shop.app.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "clients",
        uniqueConstraints = {@UniqueConstraint(name = "uk_client_name", columnNames = "client_name")})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "is_deleted")
    private boolean isDeleted;

}
