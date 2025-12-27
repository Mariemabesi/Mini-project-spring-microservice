package tn.gl.catalogservice.mapper;

import org.springframework.stereotype.Component;
import tn.gl.catalogservice.domain.Category;
import tn.gl.catalogservice.domain.Product;
import tn.gl.catalogservice.dto.CategoryDTO;
import tn.gl.catalogservice.dto.ProductDTO;

/**
 * Manual Mapper for converting between Entities and Data Transfer Objects
 * (DTOs).
 * This component ensures decoupling between the internal database entities and
 * the external API contract.
 */
@Component
public class CatalogMapper {

    public Category toEntity(CategoryDTO.Create dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public CategoryDTO.Response toDto(Category entity) {
        CategoryDTO.Response dto = new CategoryDTO.Response();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public Product toEntity(ProductDTO.Create dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        // Category set in service
        return product;
    }

    public ProductDTO.Response toDto(Product entity) {
        ProductDTO.Response dto = new ProductDTO.Response();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }
        return dto;
    }
}
