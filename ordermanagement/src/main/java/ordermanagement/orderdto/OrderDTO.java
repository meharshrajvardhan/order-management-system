package ordermanagement.orderdto;

import ordermanagement.orderentity.OrderEntity.OrderStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {

    // ── Request DTO ──────────────────────────────────────────────────────────

    public static class Request {

        @NotBlank(message = "Customer name is required")
        private String customerName;

        @NotBlank(message = "Product name is required")
        private String productName;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        private BigDecimal price;

        private OrderStatus orderStatus;

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public OrderStatus getOrderStatus() { return orderStatus; }
        public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    }

    // ── Response DTO ─────────────────────────────────────────────────────────

    public static class Response {

        private Long id;
        private String customerName;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal amount;       // price × quantity (auto-calculated)
        private OrderStatus orderStatus;
        private LocalDateTime createdDate;

        public Response() {}

        public Response(Long id, String customerName, String productName,
                        Integer quantity, BigDecimal price, BigDecimal amount,
                        OrderStatus orderStatus, LocalDateTime createdDate) {
            this.id = id;
            this.customerName = customerName;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
            this.amount = amount;
            this.orderStatus = orderStatus;
            this.createdDate = createdDate;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        public OrderStatus getOrderStatus() { return orderStatus; }
        public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

        public LocalDateTime getCreatedDate() { return createdDate; }
        public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    }
}