package tn.gl.orderservice.dto;

import lombok.Data;

@Data
public class CreateOrderDTO {
    private String customerName;
    private Long productId;
    private int quantity;
}
