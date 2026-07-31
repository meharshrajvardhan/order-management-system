package ordermanagement.orderservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ordermanagement.orderdto.OrderRequest;
import ordermanagement.orderdto.OrderResponse;
import ordermanagement.orderentity.OrderEntity;
import ordermanagement.orderentity.UserEntity;
import ordermanagement.orderentity.UserEntity.Role;
import ordermanagement.orderrepository.OrderRepository;
import ordermanagement.orderrepository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderAndCalculateAmountSuccessfully() {

        // Arrange
        String authenticatedUsername = "Harsh";

        UserEntity loggedInUser = new UserEntity();
        loggedInUser.setId(10L);
        loggedInUser.setUsername(authenticatedUsername);
        loggedInUser.setPassword("encoded-password");
        loggedInUser.setRole(Role.USER);

        OrderRequest request = new OrderRequest();

        /*
         * Even if customerName is supplied by the client,
         * the service must use the authenticated username.
         */
        request.setCustomerName("Fake Customer");
        request.setProductName("Laptop");
        request.setQuantity(2);
        request.setPrice(new BigDecimal("1000.00"));

        when(userRepository.findByUsername(authenticatedUsername))
                .thenReturn(Optional.of(loggedInUser));

        when(orderRepository.save(any(OrderEntity.class)))
                .thenAnswer(invocation -> {

                    OrderEntity savedEntity =
                            invocation.getArgument(0);

                    savedEntity.setId(1L);

                    return savedEntity;
                });

        // Act
        OrderResponse actualOrder =
                orderService.createOrder(
                        request,
                        authenticatedUsername);

        // Assert response
        assertNotNull(actualOrder);
        assertEquals(1L, actualOrder.getId());

        /*
         * Customer name must come from the authenticated user,
         * not from request.customerName.
         */
        assertEquals(
                authenticatedUsername,
                actualOrder.getCustomerName());

        assertEquals(
                "Laptop",
                actualOrder.getProductName());

        assertEquals(
                2,
                actualOrder.getQuantity());

        assertEquals(
                new BigDecimal("1000.00"),
                actualOrder.getPrice());

        assertEquals(
                new BigDecimal("2000.00"),
                actualOrder.getAmount());

        assertEquals(
                OrderEntity.OrderStatus.PENDING,
                actualOrder.getOrderStatus());

        // Capture the entity sent to the repository
        ArgumentCaptor<OrderEntity> entityCaptor =
                ArgumentCaptor.forClass(OrderEntity.class);

        verify(orderRepository, times(1))
                .save(entityCaptor.capture());

        OrderEntity entitySentToRepository =
                entityCaptor.getValue();

        // Verify ownership
        assertSame(
                loggedInUser,
                entitySentToRepository.getUser());

        assertEquals(
                authenticatedUsername,
                entitySentToRepository.getCustomerName());

        assertEquals(
                "Laptop",
                entitySentToRepository.getProductName());

        assertEquals(
                2,
                entitySentToRepository.getQuantity());

        assertEquals(
                new BigDecimal("1000.00"),
                entitySentToRepository.getPrice());

        assertEquals(
                new BigDecimal("2000.00"),
                entitySentToRepository.getAmount());

        assertEquals(
                OrderEntity.OrderStatus.PENDING,
                entitySentToRepository.getOrderStatus());

        // Verify user lookup
        verify(userRepository, times(1))
                .findByUsername(authenticatedUsername);
    }
}