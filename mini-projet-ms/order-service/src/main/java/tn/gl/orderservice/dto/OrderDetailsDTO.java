package tn.gl.orderservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDetailsDTO {
    private Long orderId;
    private String customerName;
    private double total;
    private List<LineItem> lines;

    @Data
    public static class LineItem {
        private Long productId;
        private String productName;
        private int quantity;
        private double unitPrice;
    }
}
