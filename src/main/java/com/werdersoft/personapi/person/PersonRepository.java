package com.werdersoft.personapi.person;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {

    Person findPersonByExternalID(Integer externalID);

    @Query("SELECT per.externalID FROM Person per WHERE per.externalID IS NOT NULL")
    List<Integer> findPersonsWhereExternalIdIsFilled();

}
