package com.example.auth.service;

import com.example.auth.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeQueryService {


    private final MongoTemplate mongoTemplate;

    public List<Employee> advancedSearch(
            String department, String skill, boolean activeOnly) {

        Query query = new Query();

        if (department != null) {
            query.addCriteria(Criteria.where("department").is(department));
        }
        if (skill != null) {
            query.addCriteria(Criteria.where("skills").in(skill));
        }
        if (activeOnly) {
            query.addCriteria(Criteria.where("active").is(true));
        }

        // Sort by name ascending
        query.with(Sort.by(Sort.Direction.ASC, "name"));

        // Pagination: skip first 10, return next 10
        query.skip(0).limit(10);

        return mongoTemplate.find(query, Employee.class);
    }

    // Partial update — only update specific fields
    public void updateDepartment(String id, String newDept) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("department", newDept);
        mongoTemplate.updateFirst(query, update, Employee.class);
    }



    // Option 1: @Query annotation — write MongoDB JSON query directly
   /* @Repository
    public interface EmployeeRepository extends MongoRepository<Employee, String> {

        // Find active employees in a specific department
        @Query("{ 'department': ?0, 'active': true }")
        List<Employee> findActiveByDepartment(String department);

        // Find employees who have a specific skill (array contains)
        @Query("{ 'skills': { $in: [?0] } }")
        List<Employee> findBySkill(String skill);

        // Find by city inside nested address object
        @Query("{ 'address.city': ?0 }")
        List<Employee> findByCity(String city);

        // Return only specific fields (projection)
        @Query(value = "{ 'department': ?0 }", fields = "{ 'name': 1, 'email': 1 }")
        List<Employee> findNameAndEmailByDepartment(String department);
    }*/
}
