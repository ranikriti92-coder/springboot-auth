package com.example.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "employees")  // ← maps to "employees" collection in MongoDB
@Data                                // Lombok: auto-generates getters/setters
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id                              // ← maps to MongoDB's _id field
    private String id;               // String because MongoDB ObjectId is a string

    private String employeeId;

    private String name;

    private String email;

    private String department;

    // Array field — stored as a JSON array in MongoDB
    private List<String> skills;

    // Nested object — stored inside the same document
    private Address address;

    private boolean active = true;

    private LocalDateTime joinedAt = LocalDateTime.now();

    // Inner class for nested document
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String city;
        private String country;
    }
}
