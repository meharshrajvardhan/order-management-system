package ordermanagement.orderrepository;

import ordermanagement.orderentity.OrderEntity;
import ordermanagement.orderentity.OrderEntity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByCustomerName(String customerName);

    List<OrderEntity> findByOrderStatus(OrderStatus orderStatus);

    List<OrderEntity> findByCustomerNameContainingIgnoreCase(String keyword);

    List<OrderEntity> findByProductNameContainingIgnoreCase(String keyword);
}