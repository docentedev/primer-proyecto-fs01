package com.primerproyecto.api.dto;

import lombok.Data;

@Data
public class ProductDTO {
    Long id;
    String name;
    String description;
    Integer price;
}
