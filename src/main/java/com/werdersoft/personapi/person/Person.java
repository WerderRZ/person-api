package com.werdersoft.personapi.person;

import com.werdersoft.personapi.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "person")
public class Person extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;
  
    @Column(name = "external_id")
    private Integer externalID;

    @Column(name = "email")
    private String email;

    @Builder
    public Person(UUID id, String name, Integer age) {
        super(id);
        this.name = name;
        this.age = age;
    }

}
