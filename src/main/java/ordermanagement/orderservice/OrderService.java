package ordermanagement.orderservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ordermanagement.orderdto.OrderResponse;

import org.springframework.stereotype.Service;

import ordermanagement.orderdto.OrderRequest;

import ordermanagement.orderentity.OrderEntity;
import ordermanagement.orderentity.OrderEntity.OrderStatus;
import ordermanagement.orderrepository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    public OrderResponse createOrder(OrderRequest request) {

        OrderEntity order = new OrderEntity();

        order.setCustomerName(request.getCustomerName());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        // Auto-calculate amount = price × quantity
        order.setAmount(
                request.getPrice().multiply(
                        BigDecimal.valueOf(request.getQuantity())));

        order.setOrderStatus(
                request.getOrderStatus() != null
                        ? request.getOrderStatus()
                        : OrderStatus.PENDING);

        return toResponse(orderRepository.save(order));
    }
    
    public Page<OrderResponse> getAllOrders(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderRepository.findAll(pageable)
                .map(this::toResponse);
    }

    // ── READ ALL ─────────────────────────────────────────────────────────────

    public Page<OrderResponse> getAllOrders(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return orderRepository
                .findAll(pageable)
                .map(this::toResponse);
    }

    // ── READ BY ID ───────────────────────────────────────────────────────────

    public OrderResponse getOrderById(Long id) {

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        return toResponse(order);
    }

    // ── READ BY STATUS ───────────────────────────────────────────────────────

    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {

        return orderRepository.findByOrderStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── READ BY CUSTOMER ─────────────────────────────────────────────────────

    public List<OrderResponse> getOrdersByCustomer(String customerName) {

        return orderRepository
                .findByCustomerNameContainingIgnoreCase(customerName)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    public OrderResponse updateOrder(Long id, OrderRequest request) {

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        order.setCustomerName(request.getCustomerName());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        order.setAmount(
                request.getPrice().multiply(
                        BigDecimal.valueOf(request.getQuantity())));

        if (request.getOrderStatus() != null) {
            order.setOrderStatus(request.getOrderStatus());
        }

        return toResponse(orderRepository.save(order));
    }

    // ── UPDATE STATUS ────────────────────────────────────────────────────────

    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

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

    // ── ENTITY → RESPONSE DTO ────────────────────────────────────────────────

    private OrderResponse toResponse(OrderEntity order) {

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getAmount(),
                order.getOrderStatus(),
                order.getCreatedDate()
        );
    }
}