package com.werdersoft.personapi.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.werdersoft.personapi.employee.Employee;
import com.werdersoft.personapi.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "person")
public class Person extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Employee employee;

    public PersonDTO toPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(getId());
        personDTO.setName(getName());
        personDTO.setAge(getAge());
        personDTO.setEmployee_id(employee == null ? null : employee.getId());
        return personDTO;
    }

}
