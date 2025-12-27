package tn.gl.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.gl.orderservice.dto.ProductDTO;

@FeignClient(name = "catalog-service")
public interface CatalogClient {
    @GetMapping("/catalog/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
