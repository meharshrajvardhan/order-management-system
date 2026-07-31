package ordermanagement.ordercontroller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {

        Map<String, String> response = new LinkedHashMap<>();

        response.put(
            "application",
            "Cloud-Native Order Management System"
        );

        response.put(
            "status",
            "Application is running successfully"
        );

        response.put(
            "description",
            "Secure REST API built with Java, Spring Boot, JWT, PostgreSQL and Docker"
        );

        response.put(
            "apiDocumentation",
            "/swagger-ui/index.html"
        );

        response.put(
            "healthCheck",
            "/healthz"
        );

        return response;
    }
}