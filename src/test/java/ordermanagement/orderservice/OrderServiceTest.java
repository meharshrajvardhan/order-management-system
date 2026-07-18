package ordermanagement.orderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ordermanagement.orderdto.OrderRequest;
import ordermanagement.orderdto.OrderResponse;
import ordermanagement.orderentity.OrderEntity;
import ordermanagement.orderrepository.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderAndCalculateAmountSuccessfully() {

        // Arrange: request received from the client
        OrderRequest request = new OrderRequest();
        request.setCustomerName("Harsh");
        request.setProductName("Laptop");
        request.setQuantity(2);
        request.setPrice(new BigDecimal("1000.00"));

        /*
         * Pretend that PostgreSQL saved the entity.
         * Mockito returns the same entity received from OrderService
         * after assigning a fake database ID.
         */
        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> {
                    OrderEntity savedEntity = invocation.getArgument(0);
                    savedEntity.setId(1L);
                    return savedEntity;
                });

        // Act: execute the actual business logic
        OrderResponse actualOrder = orderService.createOrder(request);

        // Assert: verify returned response
        assertNotNull(actualOrder);
        assertEquals(1L, actualOrder.getId());
        assertEquals("Harsh", actualOrder.getCustomerName());
        assertEquals("Laptop", actualOrder.getProductName());
        assertEquals(2, actualOrder.getQuantity());
        assertEquals(new BigDecimal("1000.00"), actualOrder.getPrice());
        assertEquals(new BigDecimal("2000.00"), actualOrder.getAmount());

        // Verify that repository.save() was called exactly once
        verify(orderRepository, times(1))
                .save(any(OrderEntity.class));

        // Capture and inspect the entity passed to the repository
        ArgumentCaptor<OrderEntity> entityCaptor =
                ArgumentCaptor.forClass(OrderEntity.class);

        verify(orderRepository, times(1))
                .save(entityCaptor.capture());

        OrderEntity entitySentToRepository = entityCaptor.getValue();

        assertEquals("Harsh", entitySentToRepository.getCustomerName());
        assertEquals("Laptop", entitySentToRepository.getProductName());
        assertEquals(2, entitySentToRepository.getQuantity());
        assertEquals(
                new BigDecimal("2000.00"),
                entitySentToRepository.getAmount()
        );
    }
}