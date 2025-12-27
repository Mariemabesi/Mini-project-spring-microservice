package tn.gl.orderservice.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private Long categoryId;
    private String categoryName;
}
