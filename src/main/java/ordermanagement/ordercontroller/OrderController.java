package ordermanagement.ordercontroller;

import ordermanagement.orderdto.OrderDTO;
import ordermanagement.orderentity.OrderEntity.OrderStatus;
import ordermanagement.orderservice.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /api/orders
    @PostMapping
    public ResponseEntity<OrderDTO.Response> createOrder(
            @Valid @RequestBody OrderDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    // GET /api/orders?status=PROCESSING&customer=Amma
    @GetMapping
    public ResponseEntity<List<OrderDTO.Response>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String customer) {

        if (status != null) return ResponseEntity.ok(orderService.getOrdersByStatus(status));
        if (customer != null && !customer.isBlank()) return ResponseEntity.ok(orderService.getOrdersByCustomer(customer));
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /api/orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO.Response> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // PUT /api/orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO.Response> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO.Request request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    // PATCH /api/orders/{id}/status?status=SHIPPED
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO.Response> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    // DELETE /api/orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}