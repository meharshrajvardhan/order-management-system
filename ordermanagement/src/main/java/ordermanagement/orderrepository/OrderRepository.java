package ordermanagement.orderrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ordermanagement.orderentity.OrderEntity;
import ordermanagement.orderentity.OrderEntity.OrderStatus;

@Repository
public interface OrderRepository
        extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByOrderStatus(
            OrderStatus orderStatus);

    List<OrderEntity>
    findByCustomerNameContainingIgnoreCase(
            String keyword);

    List<OrderEntity>
    findByProductNameContainingIgnoreCase(
            String keyword);

    /*
     * Fetch only orders owned by the authenticated user.
     */
    List<OrderEntity> findByUser_Username(
            String username);

    /*
     * Fetch an order only if both the order ID and owner match.
     */
    Optional<OrderEntity> findByIdAndUser_Username(
            Long id,
            String username);
}