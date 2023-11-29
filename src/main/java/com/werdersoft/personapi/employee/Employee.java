package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.enums.Position;
import com.werdersoft.personapi.person.Person;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "employee")
public class Employee extends BaseEntity {

    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "salary")
    private BigDecimal salary;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", unique = true, nullable = false)
    private Person person;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subdivision_id", nullable = false)
    private Subdivision subdivision;

    public EmployeeDTOResponse toEmployeeResponse() {
        EmployeeDTOResponse employeeDTO = new EmployeeDTOResponse();
        employeeDTO.setId(getId());
        employeeDTO.setPosition(getPosition());
        employeeDTO.setSalary(getSalary());
        employeeDTO.setPerson_id(person == null ? null : person.getId());
        employeeDTO.setSubdivision_id(subdivision == null ? null : subdivision.getId());
        return employeeDTO;
    }

}