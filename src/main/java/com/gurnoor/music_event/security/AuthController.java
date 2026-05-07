package com.gurnoor.music_event.security;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

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