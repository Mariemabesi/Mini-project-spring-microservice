package tn.gl.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.gl.orderservice.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
