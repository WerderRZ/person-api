package com.werdersoft.personapi.person;

import com.werdersoft.personapi.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    public Person(UUID id, String name, Integer age, Integer externalID, String email) {
        super(id);
        this.name = name;
        this.age = age;
        this.externalID = externalID;
        this.email = email;
    }

}
