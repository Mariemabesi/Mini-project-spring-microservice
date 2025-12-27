package tn.gl.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.gl.orderservice.client.CatalogClient;
import tn.gl.orderservice.domain.Order;
import tn.gl.orderservice.domain.OrderLine;
import tn.gl.orderservice.dto.CreateOrderDTO;
import tn.gl.orderservice.dto.OrderDetailsDTO;
import tn.gl.orderservice.dto.ProductDTO;
import tn.gl.orderservice.repo.OrderRepository;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;

    /**
     * Creates a new order.
     * This method implements Synchronous Communication using OpenFeign.
     * 
     * Logic:
     * 1. It calls the Catalog Service (via CatalogClient) to verify product
     * existence and fetch the current price.
     * 2. It calculates the total price based on the fetched price (not user input)
     * for consistency.
     * 3. It saves the order and order lines to the local database.
     * 
     * @param dto Data Transfer Object containing customer and product details.
     * @return The created Order entity.
     */
    public Order createOrder(CreateOrderDTO dto) {
        // 1. Fetch product price synchronously from Catalog Service
        // This ensures validity of product and real-time price accuracy.
        ProductDTO product = catalogClient.getProductById(dto.getProductId());

        // 2. Compute total
        double total = product.getPrice() * dto.getQuantity();

        // 3. Create Order
        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setTotal(total);

        // 4. Create Line
        OrderLine line = new OrderLine();
        line.setProductId(dto.getProductId());
        line.setQuantity(dto.getQuantity());
        line.setUnitPrice(product.getPrice());
        line.setOrder(order);

        order.setLines(Collections.singletonList(line));

        return orderRepository.save(order);
    }

    public OrderDetailsDTO getOrderDetails(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        OrderDetailsDTO details = new OrderDetailsDTO();
        details.setOrderId(order.getId());
        details.setCustomerName(order.getCustomerName());
        details.setTotal(order.getTotal());

        details.setLines(order.getLines().stream().map(line -> {
            OrderDetailsDTO.LineItem item = new OrderDetailsDTO.LineItem();
            item.setProductId(line.getProductId());
            item.setQuantity(line.getQuantity());
            item.setUnitPrice(line.getUnitPrice());

            // Call Catalog to get name
            try {
                ProductDTO p = catalogClient.getProductById(line.getProductId());
                item.setProductName(p.getName());
            } catch (Exception e) {
                item.setProductName("Unknown Product (Service Down)");
            }
            return item;
        }).collect(Collectors.toList()));

        return details;
    }
}
