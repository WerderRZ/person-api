package com.werdersoft.personapi.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("SELECT emp FROM Employee emp WHERE emp.id IN (:employeesIds)")
    Set<Employee> findEmployeesByEmployeesIds(List<UUID> employeesIds);
}
