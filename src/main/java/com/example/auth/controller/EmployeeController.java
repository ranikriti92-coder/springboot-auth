package com.example.auth.controller;

import com.example.auth.entity.Employee;
import com.example.auth.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // POST /api/employees — create new employee
    // Body: { "name":"Priya","email":"p@x.com","department":"Engineering" }
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.create(employee));
    }

    // GET /api/employees — get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    // GET /api/employees/{id} — get one by MongoDB _id
    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    // GET /api/employees/department/{dept}
    @GetMapping("/department/{dept}")
    public ResponseEntity<List<Employee>> findByDepartment(@PathVariable String dept) {
        return ResponseEntity.ok(employeeService.findByDepartment(dept));
    }

    // GET /api/employees/search?name=priya
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> search(@RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchByName(name));
    }

    // PUT /api/employees/{id} — full update
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(
            @PathVariable String id,
            @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.update(id, employee));
    }

    // DELETE /api/employees/{id} — delete by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        employeeService.delete(id);
        return ResponseEntity.ok("Employee " + id + " deleted successfully");
    }
}
