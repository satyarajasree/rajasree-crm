package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmployeeReferenceId(String employeeReferenceId);

}
