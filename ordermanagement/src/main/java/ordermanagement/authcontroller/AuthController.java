package ordermanagement.authcontroller;
import ordermanagement.orderdto.Authdto;
import ordermanagement.orderentity.UserEntity;
import ordermanagement.orderentity.UserEntity.Role;
import jakarta.validation.Valid;
import ordermanagement.orderrepository.UserRepository;
import ordermanagement.security.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Authdto.Request request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        Role role = Role.USER; // default role
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            role = Role.ADMIN;
        }

        UserEntity user = new UserEntity(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                role
        );
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully as " + role);
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Authdto.Request request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            UserEntity user = userRepository.findByUsername(request.getUsername()).get();
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            return ResponseEntity.ok(new Authdto.Response(
                    token,
                    user.getUsername(),
                    user.getRole().name(),
                    "Login successful"
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}