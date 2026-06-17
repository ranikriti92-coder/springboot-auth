package com.example.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demonstrates role-based authorization.
 * All endpoints require a valid JWT; roles further restrict access.
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /** Any authenticated user */
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("Hello User! You have USER access.");
    }

    /** MODERATOR or ADMIN only */
    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> moderatorAccess() {
        return ResponseEntity.ok("Hello Moderator! You have MODERATOR access.");
    }

    /** ADMIN only */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Hello Admin! You have ADMIN access.");
    }
}
