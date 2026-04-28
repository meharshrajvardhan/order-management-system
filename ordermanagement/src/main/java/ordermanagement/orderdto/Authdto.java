package ordermanagement.orderdto;

import jakarta.validation.constraints.NotBlank;

public class Authdto {

    // ── Register / Login Request ─────────────────────────────────────────────
    public static class Request {

        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        private String role; // "ADMIN" or "USER" — only used during register

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    // ── Login Response ───────────────────────────────────────────────────────
    public static class Response {
        private String token;
        private String username;
        private String role;
        private String message;

        public Response(String token, String username, String role, String message) {
            this.token = token;
            this.username = username;
            this.role = role;
            this.message = message;
        }

        public String getToken() { return token; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getMessage() { return message; }
    }
}