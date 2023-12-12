package com.werdersoft.personapi.employee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EmployeeRepository extends CrudRepository<Employee, UUID> {

    @Query("SELECT emp FROM Employee emp WHERE emp.id = :employeesIds")
    Set<Employee> findEmployeesByEmployeesIds(List<UUID> employeesIds);
}
