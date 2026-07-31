package ordermanagement.ordercontroller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ordermanagement.orderdto.OrderRequest;
import ordermanagement.orderdto.OrderResponse;
import ordermanagement.orderentity.OrderEntity.OrderStatus;
import ordermanagement.orderservice.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // USER/ADMIN: Create an order for the logged-in account
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {

        OrderResponse createdOrder =
                orderService.createOrder(
                        request,
                        authentication.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    // USER/ADMIN: View only their own orders
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            Authentication authentication) {

        return ResponseEntity.ok(
                orderService.getMyOrders(
                        authentication.getName()));
    }

    // USER/ADMIN: View one order only when they own it
    @GetMapping("/my-orders/{id}")
    public ResponseEntity<OrderResponse> getMyOrderById(
            @PathVariable(name = "id") Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                orderService.getMyOrderById(
                        id,
                        authentication.getName()));
    }

    // ADMIN: View every order
    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(
                    name = "status",
                    required = false)
            OrderStatus status,

            @RequestParam(
                    name = "customer",
                    required = false)
            String customer,

            @RequestParam(
                    name = "page",
                    defaultValue = "0")
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "5")
            int size,

            @RequestParam(
                    name = "sortBy",
                    defaultValue = "id")
            String sortBy,

            @RequestParam(
                    name = "direction",
                    defaultValue = "asc")
            String direction) {

        if (status != null) {
            return ResponseEntity.ok(
                    orderService.getOrdersByStatus(status));
        }

        if (customer != null && !customer.isBlank()) {
            return ResponseEntity.ok(
                    orderService.getOrdersByCustomer(customer));
        }

        return ResponseEntity.ok(
                orderService.getAllOrders(
                        page,
                        size,
                        sortBy,
                        direction));
    }

    // ADMIN: View any order
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id));
    }

    // ADMIN: Update an order
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody OrderRequest request) {

        return ResponseEntity.ok(
                orderService.updateOrder(id, request));
    }

    // ADMIN: Update an order's status
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "status")
            OrderStatus status) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        id,
                        status));
    }

    // ADMIN: Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable(name = "id") Long id) {

        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }
}