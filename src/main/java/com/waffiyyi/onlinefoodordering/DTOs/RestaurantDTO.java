package com.waffiyyi.onlinefoodordering.DTOs;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.Length;

import java.util.List;

@Data
@Embeddable
public class RestaurantDTO {
    private String tittle;
    @Column(length = 1000)
    @ElementCollection
    private List<String> images;
    private String description;
    private Long id;
}
