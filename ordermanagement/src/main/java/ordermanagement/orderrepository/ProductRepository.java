package ordermanagement.orderrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ordermanagement.orderentity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrBrandContainingIgnoreCase(
            String name,
            String category,
            String brand
    );
}