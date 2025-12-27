package tn.gl.catalogservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.gl.catalogservice.dto.CategoryDTO;
import tn.gl.catalogservice.dto.ProductDTO;
import tn.gl.catalogservice.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @PostMapping("/categories")
    public CategoryDTO.Response createCategory(@RequestBody CategoryDTO.Create dto) {
        return catalogService.createCategory(dto);
    }

    @GetMapping("/categories")
    public List<CategoryDTO.Response> getAllCategories() {
        return catalogService.getAllCategories();
    }

    @PostMapping("/products")
    public ProductDTO.Response createProduct(@RequestBody ProductDTO.Create dto) {
        return catalogService.createProduct(dto);
    }

    @GetMapping("/products")
    public List<ProductDTO.Response> getAllProducts() {
        return catalogService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ProductDTO.Response getProductById(@PathVariable Long id) {
        return catalogService.getProductById(id);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        catalogService.deleteProduct(id);
    }
}
