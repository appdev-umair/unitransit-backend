package com.netsflow.unitransit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netsflow.unitransit.security.JwtTokenUtil;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AdminController {

    private final JwtTokenUtil jwtTokenUtil;

    // Constructor injection
    public AdminController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/admin/task")
    public ResponseEntity<String> performAdminTask(@RequestHeader("Authorization") String token) {
        // Ensure the token is prefixed with "Bearer "
        if (token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer "
            String role = jwtTokenUtil.extractRole(jwtToken);
            System.out.println(role);
            if (!"ADMIN".equals(role)) {
                throw new IllegalAccessError("You are not authorized to perform this action.");
            }

            return ResponseEntity.ok("Admin task performed successfully.");
        } else {
            throw new IllegalAccessError("Authorization header is missing or incorrect.");
        }
    }
}