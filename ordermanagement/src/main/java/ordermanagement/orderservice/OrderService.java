package ordermanagement.orderservice;

import ordermanagement.orderdto.OrderDTO;
import ordermanagement.orderentity.OrderEntity;
import ordermanagement.orderentity.OrderEntity.OrderStatus;
import ordermanagement.orderrepository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    public OrderDTO.Response createOrder(OrderDTO.Request request) {
        OrderEntity order = new OrderEntity();
        order.setCustomerName(request.getCustomerName());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        // Auto-calculate amount = price × quantity
        order.setAmount(request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setOrderStatus(request.getOrderStatus() != null ? request.getOrderStatus() : OrderStatus.PENDING);

        return toResponse(orderRepository.save(order));
    }

    // ── READ ALL ─────────────────────────────────────────────────────────────

    public List<OrderDTO.Response> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── READ BY ID ───────────────────────────────────────────────────────────

    public OrderDTO.Response getOrderById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return toResponse(order);
    }

    // ── READ BY STATUS ───────────────────────────────────────────────────────

    public List<OrderDTO.Response> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── READ BY CUSTOMER ─────────────────────────────────────────────────────

    public List<OrderDTO.Response> getOrdersByCustomer(String customerName) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(customerName).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    public OrderDTO.Response updateOrder(Long id, OrderDTO.Request request) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setCustomerName(request.getCustomerName());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setAmount(request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        if (request.getOrderStatus() != null) {
            order.setOrderStatus(request.getOrderStatus());
        }

        return toResponse(orderRepository.save(order));
    }

    // ── UPDATE STATUS ONLY ───────────────────────────────────────────────────

    public OrderDTO.Response updateOrderStatus(Long id, OrderStatus status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setOrderStatus(status);
        return toResponse(orderRepository.save(order));
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    // ── MAPPER ───────────────────────────────────────────────────────────────

    private OrderDTO.Response toResponse(OrderEntity o) {
        return new OrderDTO.Response(
                o.getId(),
                o.getCustomerName(),
                o.getProductName(),
                o.getQuantity(),
                o.getPrice(),
                o.getAmount(),
                o.getOrderStatus(),
                o.getCreatedDate()
        );
    }
}