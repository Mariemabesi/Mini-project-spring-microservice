package tn.gl.catalogservice.dto;

import lombok.Data;

public class ProductDTO {
    @Data
    public static class Create {
        private String name;
        private double price;
        private Long categoryId;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private double price;
        private Long categoryId;
        private String categoryName;
    }
}
