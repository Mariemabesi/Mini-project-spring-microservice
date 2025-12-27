package tn.gl.catalogservice.dto;

import lombok.Data;

public class CategoryDTO {
    @Data
    public static class Create {
        private String name;
    }
    
    @Data
    public static class Response {
        private Long id;
        private String name;
    }
}
