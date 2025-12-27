package tn.gl.authservice.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.gl.authservice.domain.Role;
import tn.gl.authservice.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody AuthRequest request) {
        return authService.saveUser(request.email, request.password, request.role);
    }

    @PostMapping("/signin")
    public Map<String, String> signin(@RequestBody AuthRequest request) {
        String token = authService.generateToken(request.email, request.password);
        return Map.of("token", token);
    }

    @Data
    static class AuthRequest {
        private String email;
        private String password;
        private Role role;
    }
}
