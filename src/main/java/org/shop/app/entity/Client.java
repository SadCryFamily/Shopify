package org.shop.app.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NamedEntityGraph(name = "client.roles", attributeNodes = @NamedAttributeNode("roles"))
@Table(name = "clients",
        uniqueConstraints = {@UniqueConstraint(name = "uk_client_name", columnNames = "client_name")})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_password")
    private String clientPassword;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "client_roles",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Role> roles;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order) {
        order.setClient(this);
        orders.add(order);
    }

}
