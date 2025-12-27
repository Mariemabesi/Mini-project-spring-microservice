package tn.gl.catalogservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.gl.catalogservice.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
