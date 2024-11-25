package com.a3.clinica.controller;

import com.a3.clinica.dto.LoginRequest;
import com.a3.clinica.model.User;
import com.a3.clinica.service.JwtService;
import com.a3.clinica.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = userService.findByUsername(request.getUsername());
        if (user.isPresent() && userService.getPasswordEncoder().matches(request.getPassword(), user.get().getPassword())) {
            String token = jwtService.generateToken(user.get());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(401).body("Bad Credentials");
    }

}

class AuthResponse {
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
