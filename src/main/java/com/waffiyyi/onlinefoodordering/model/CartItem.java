package com.waffiyyi.onlinefoodordering.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    private Food food;

    private int quantity;


    @ElementCollection
    private List<String> ingredients;

    private Long totalPrice;

}
