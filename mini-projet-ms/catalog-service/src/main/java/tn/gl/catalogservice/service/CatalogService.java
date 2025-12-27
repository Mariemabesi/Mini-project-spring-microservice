package tn.gl.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.gl.catalogservice.domain.Category;
import tn.gl.catalogservice.domain.Product;
import tn.gl.catalogservice.dto.CategoryDTO;
import tn.gl.catalogservice.dto.ProductDTO;
import tn.gl.catalogservice.mapper.CatalogMapper;
import tn.gl.catalogservice.repo.CategoryRepository;
import tn.gl.catalogservice.repo.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CatalogMapper catalogMapper;

    public CategoryDTO.Response createCategory(CategoryDTO.Create dto) {
        Category category = catalogMapper.toEntity(dto);
        return catalogMapper.toDto(categoryRepository.save(category));
    }

    public List<CategoryDTO.Response> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(catalogMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDTO.Response createProduct(ProductDTO.Create dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = catalogMapper.toEntity(dto);
        product.setCategory(category);
        return catalogMapper.toDto(productRepository.save(product));
    }

    public List<ProductDTO.Response> getAllProducts() {
        return productRepository.findAll().stream()
                .map(catalogMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDTO.Response getProductById(Long id) {
        return productRepository.findById(id)
                .map(catalogMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
