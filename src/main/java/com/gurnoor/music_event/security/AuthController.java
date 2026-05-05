package com.gurnoor.music_event.security;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    // Hardcoded users for demo — replace with DB in production
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if ("organizer".equals(username) && "pass123".equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "token", jwtUtil.generateToken(username, "ORGANIZER")));
        } else if ("viewer".equals(username) && "pass123".equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "token", jwtUtil.generateToken(username, "VIEWER")));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}