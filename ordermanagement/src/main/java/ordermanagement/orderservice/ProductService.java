package ordermanagement.orderservice;

import java.util.List;

import org.springframework.stereotype.Service;

import ordermanagement.orderentity.ProductEntity;
import ordermanagement.orderrepository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity addProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductEntity> searchProducts(String keyword) {
        return productRepository
                .findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrBrandContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                );
    }
}