package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.enums.Position;
import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.subdivision.Subdivision;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class EmployeeDTORequest extends EmployeeDTO {

    public Employee toEmployeeEntity(Person person, Subdivision subdivision) {

        Employee employee = new Employee();
        employee.setPosition(getPosition());
        employee.setSalary(getSalary());
        employee.setPerson(person);
        employee.setSubdivision(subdivision);

        return employee;
    }
}
