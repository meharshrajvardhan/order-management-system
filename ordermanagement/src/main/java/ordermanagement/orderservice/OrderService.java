package ordermanagement.orderservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ordermanagement.orderdto.OrderRequest;
import ordermanagement.orderdto.OrderResponse;
import ordermanagement.orderentity.OrderEntity;
import ordermanagement.orderentity.OrderEntity.OrderStatus;
import ordermanagement.orderentity.UserEntity;
import ordermanagement.orderrepository.OrderRepository;
import ordermanagement.orderrepository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // ── CREATE OWN ORDER ──────────────────────────────────────

    public OrderResponse createOrder(
            OrderRequest request,
            String authenticatedUsername) {

        UserEntity loggedInUser = userRepository
                .findByUsername(authenticatedUsername)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found: "
                                        + authenticatedUsername));

        OrderEntity order = new OrderEntity();

        // Connect the order with the logged-in user.
        order.setUser(loggedInUser);

        /*
         * Never trust customerName supplied by the frontend.
         * The verified username comes from the JWT.
         */
        order.setCustomerName(loggedInUser.getUsername());

        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        order.setAmount(
                request.getPrice().multiply(
                        BigDecimal.valueOf(
                                request.getQuantity())));

        /*
         * A customer cannot create CONFIRMED, SHIPPED or
         * DELIVERED orders directly.
         */
        order.setOrderStatus(OrderStatus.PENDING);

        return toResponse(orderRepository.save(order));
    }

    // ── READ OWN ORDERS ───────────────────────────────────────

    public List<OrderResponse> getMyOrders(
            String authenticatedUsername) {

        return orderRepository
                .findByUser_Username(authenticatedUsername)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── READ ONE OWN ORDER ────────────────────────────────────

    public OrderResponse getMyOrderById(
            Long id,
            String authenticatedUsername) {

        OrderEntity order = orderRepository
                .findByIdAndUser_Username(
                        id,
                        authenticatedUsername)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found or you do not "
                                        + "have permission to view it"));

        return toResponse(order);
    }

    // ── ADMIN: READ ALL ───────────────────────────────────────

    public Page<OrderResponse> getAllOrders(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderRepository
                .findAll(pageable)
                .map(this::toResponse);
    }

    public Page<OrderResponse> getAllOrders(
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return orderRepository
                .findAll(pageable)
                .map(this::toResponse);
    }

    // ── ADMIN: READ BY ID ─────────────────────────────────────

    public OrderResponse getOrderById(Long id) {

        OrderEntity order = findOrderById(id);

        return toResponse(order);
    }

    // ── ADMIN: READ BY STATUS ─────────────────────────────────

    public List<OrderResponse> getOrdersByStatus(
            OrderStatus status) {

        return orderRepository
                .findByOrderStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── ADMIN: READ BY CUSTOMER ───────────────────────────────

    public List<OrderResponse> getOrdersByCustomer(
            String customerName) {

        return orderRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── ADMIN: UPDATE ORDER ───────────────────────────────────

    public OrderResponse updateOrder(
            Long id,
            OrderRequest request) {

        OrderEntity order = findOrderById(id);

        /*
         * Do not change user ownership or customerName here.
         * Otherwise an order could accidentally be reassigned.
         */
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());

        order.setAmount(
                request.getPrice().multiply(
                        BigDecimal.valueOf(
                                request.getQuantity())));

        if (request.getOrderStatus() != null) {
            order.setOrderStatus(request.getOrderStatus());
        }

        return toResponse(orderRepository.save(order));
    }

    // ── ADMIN: UPDATE STATUS ──────────────────────────────────

    public OrderResponse updateOrderStatus(
            Long id,
            OrderStatus status) {

        OrderEntity order = findOrderById(id);

        order.setOrderStatus(status);

        return toResponse(orderRepository.save(order));
    }

    // ── ADMIN: DELETE ─────────────────────────────────────────

    public void deleteOrder(Long id) {

        OrderEntity order = findOrderById(id);

        orderRepository.delete(order);
    }

    // ── SHARED PRIVATE METHOD ─────────────────────────────────

    private OrderEntity findOrderById(Long id) {

        return orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with id: " + id));
    }

    // ── ENTITY TO RESPONSE DTO ────────────────────────────────

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