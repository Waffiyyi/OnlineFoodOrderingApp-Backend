package com.waffiyyi.onlinefoodordering.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waffiyyi.onlinefoodordering.enums.ORDER_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;

    private Long totalAmount;

    @Enumerated(value = EnumType.STRING)
    private ORDER_STATUS orderStatus;

    private Date createdAt;

    @ManyToOne
    private Address deliveryAddress;


    @OneToMany
    private List<OrderItem> items;

    private int totalItem;

    private Long totalPrice;
}
