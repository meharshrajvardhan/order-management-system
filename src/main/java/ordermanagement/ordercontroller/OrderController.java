package ordermanagement.ordercontroller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // Create a new order
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        OrderResponse createdOrder =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    // Get all orders with optional filtering, pagination and sorting
    @GetMapping
    public ResponseEntity<?> getAllOrders(
            @RequestParam(name = "status", required = false)
            OrderStatus status,

            @RequestParam(name = "customer", required = false)
            String customer,

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "5")
            int size,

            @RequestParam(name = "sortBy", defaultValue = "id")
            String sortBy,

            @RequestParam(name = "direction", defaultValue = "asc")
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

    // Get a single order by ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id));
    }

    // Update all editable order details
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody OrderRequest request) {

        return ResponseEntity.ok(
                orderService.updateOrder(id, request));
    }

    // Update only the order status
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "status") OrderStatus status) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(id, status));
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable(name = "id") Long id) {

        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }
}