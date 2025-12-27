package tn.gl.catalogservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.gl.catalogservice.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
