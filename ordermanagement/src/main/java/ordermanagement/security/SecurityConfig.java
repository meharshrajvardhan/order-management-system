package ordermanagement.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
            // REST API uses JWT, not browser sessions
            .csrf(csrf -> csrf.disable())

            // Allow React frontend to call Spring Boot
            .cors(cors ->
                cors.configurationSource(
                    corsConfigurationSource())
            )

            // Do not create server-side sessions
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS)
            )

            // Return correct status codes
            .exceptionHandling(exception -> exception

                // No token or invalid authentication
                .authenticationEntryPoint(
                    new HttpStatusEntryPoint(
                        HttpStatus.UNAUTHORIZED))

                // Valid login but insufficient role
                .accessDeniedHandler(
                    (request, response, deniedException) ->
                        response.sendError(
                            HttpStatus.FORBIDDEN.value(),
                            "Access denied"))
            )

            .authorizeHttpRequests(auth -> auth

                // Allow browser CORS preflight requests
                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                )
                .permitAll()

                // Public endpoints
                .requestMatchers(
                    "/",
                    "/healthz",
                    "/api/auth/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/error"
                )
                .permitAll()

                /*
                 * USER and ADMIN can create their own orders.
                 * The authenticated username comes from the JWT.
                 */
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/orders"
                )
                .hasAnyRole("USER", "ADMIN")

                /*
                 * USER and ADMIN can view only the orders
                 * associated with their own authenticated account.
                 *
                 * These rules must appear before /api/orders/**.
                 */
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/orders/my-orders",
                    "/api/orders/my-orders/**"
                )
                .hasAnyRole("USER", "ADMIN")

                // Only ADMIN can retrieve all/arbitrary orders
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/orders",
                    "/api/orders/**"
                )
                .hasRole("ADMIN")

                // Only ADMIN can update orders
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/orders/**"
                )
                .hasRole("ADMIN")

                // Only ADMIN can partially update orders
                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/orders/**"
                )
                .hasRole("ADMIN")

                // Only ADMIN can delete orders
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/orders/**"
                )
                .hasRole("ADMIN")

                // Every other endpoint requires authentication
                .anyRequest()
                .authenticated()
            )

            // Validate JWT before Spring's username/password filter
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    /*
     * CORS configuration for the local React development server.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://127.0.0.1:5173"
                ));

        configuration.setAllowedMethods(
                List.of(
                    "GET",
                    "POST",
                    "PUT",
                    "PATCH",
                    "DELETE",
                    "OPTIONS"
                ));

        configuration.setAllowedHeaders(
                List.of(
                    "Authorization",
                    "Content-Type"
                ));

        configuration.setExposedHeaders(
                List.of("Authorization"));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration
                .getAuthenticationManager();
    }
}