package com.rajasreeit.backend.controller;


import com.rajasreeit.backend.entities.Employee;
import com.rajasreeit.backend.exceptions.EmployeeNotFoundException;
import com.rajasreeit.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employee/{referenceId}")
    public ResponseEntity<?> getEmployeeByReferenceId(@PathVariable String referenceId) {
        try {
            Employee employee = employeeService.findEmployeeByReferenceId(referenceId);
            return ResponseEntity.ok(employee);
        } catch (EmployeeNotFoundException ex) {
            return ResponseEntity.ok(ex.getMessage());
        }
    }

}
