package com.example.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    // Step 1: Create logger — do this in EVERY class you want to log
    private static final Logger log =
            LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/hello")
    public String hello() {
        log.info("📥 GET /hello was called");
        return "Hello from Spring Boot!";
    }

    @GetMapping("/user/{name}")
    public String getUser(@PathVariable String name) {
        log.info("🔍 Looking up user: {}", name);
        log.debug("📋 Debug info for user lookup: name={}", name);
        return "User: " + name;
    }

    @PostMapping("/order")
    public String createOrder(@RequestBody String body) {
        log.info("📦 New order received: {}", body);
        try {
            // Simulate some processing
            if (body.isEmpty()) {
                throw new RuntimeException("Order body is empty!");
            }
            log.info("✅ Order processed successfully");
            return "Order created!";
        } catch (Exception e) {
            log.error("❌ Order processing failed: {}", e.getMessage(), e);
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/generate-logs")
    public String generateLots() {
        // Generate several logs at once to test Kibana
        log.info ("ℹ️  INFO log from generate-logs");
        log.warn ("⚠️  WARN log — something to watch");
        log.error("❌ ERROR log — something went wrong");
        log.debug("🔍 DEBUG log — detailed info");
        return "Generated 4 logs — check Kibana!";
    }
}
