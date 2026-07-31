package ordermanagement.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ordermanagement")
@EnableJpaRepositories(basePackages = "ordermanagement.orderrepository")
@EntityScan(basePackages = "ordermanagement.orderentity")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}