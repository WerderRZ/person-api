package com.werdersoft.personapi.person;

import com.werdersoft.personapi.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO extends BaseDTO {
    private String name;
    private Integer age;

    public Person toPerson() {
        Person person = new Person();
        person.setName(getName());
        person.setAge(getAge());
        return person;
    }

}
