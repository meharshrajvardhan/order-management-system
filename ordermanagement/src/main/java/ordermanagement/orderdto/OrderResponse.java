package ordermanagement.orderdto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ordermanagement.orderentity.OrderEntity.OrderStatus;

public class OrderResponse {

    private Long id;
    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private OrderStatus orderStatus;
    private LocalDateTime createdDate;

    public OrderResponse() {
    }

    public OrderResponse(Long id,
                         String customerName,
                         String productName,
                         Integer quantity,
                         BigDecimal price,
                         BigDecimal amount,
                         OrderStatus orderStatus,
                         LocalDateTime createdDate) {

        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
        this.orderStatus = orderStatus;
        this.createdDate = createdDate;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


}