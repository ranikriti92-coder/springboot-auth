package com.example.auth.service;

import com.example.auth.entity.Employee;
import com.example.auth.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // ── CREATE ──────────────────────────────────────────────────
    public Employee create(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Email already exists: " + employee.getEmail());
        }
        Employee saved = employeeRepository.save(employee);
        log.info("Employee created with id: {}", saved.getId());
        return saved;
    }

    // ── READ ────────────────────────────────────────────────────
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    public List<Employee> findByDepartment(String dept) {
        return employeeRepository.findByDepartment(dept);
    }

    public List<Employee> searchByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    // ── UPDATE ──────────────────────────────────────────────────
    public Employee update(String id, Employee updatedEmployee) {
        Employee existing = findById(id);     // throws if not found
        existing.setName(updatedEmployee.getName());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setSkills(updatedEmployee.getSkills());
        existing.setAddress(updatedEmployee.getAddress());
        existing.setActive(updatedEmployee.isActive());
        return employeeRepository.save(existing);  // save() does UPDATE when id exists
    }

    // ── DELETE ──────────────────────────────────────────────────
    public void delete(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted: {}", id);
    }
}
