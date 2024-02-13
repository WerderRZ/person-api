package com.werdersoft.personapi.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findPersonByExternalID(Integer externalID);

    @Query("SELECT per.externalID FROM Person per WHERE per.externalID IS NOT NULL")
    List<Integer> findPersonsWhereExternalIdIsFilled();

    @Query("SELECT per FROM Person per WHERE per.id IN (:uuids)")
    List<Person> findPersonsByIds(List<UUID> uuids);

}
