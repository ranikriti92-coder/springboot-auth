package com.example.auth.repository;

import com.example.auth.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository
        extends MongoRepository<Employee, String> {
    //                           ↑          ↑
    //                      Document type  ID type (String for MongoDB)

    // ── Free methods (no code needed) ──────────────────────────
    // save(employee)          → INSERT or UPDATE
    // findById(id)            → find by _id
    // findAll()               → all documents
    // deleteById(id)          → delete by _id
    // existsById(id)          → check exists
    // count()                 → count documents

    // ── Custom derived query methods ────────────────────────────
    // Spring reads the method name and generates the MongoDB query

    List<Employee> findByDepartment(String department);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByActiveTrue();

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findBySkillsContaining(String skill);

    List<Employee> findByDepartmentAndActiveTrue(String dept);

    boolean existsByEmail(String email);

    long countByDepartment(String department);
}