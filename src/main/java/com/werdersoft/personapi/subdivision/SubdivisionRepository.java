package com.werdersoft.personapi.subdivision;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface SubdivisionRepository extends CrudRepository<Subdivision, UUID> {

    @Query("SELECT sub FROM Subdivision sub WHERE sub.id IN (:subdivisionsIds)")
    Set<Subdivision> findSubdivisionsBySubdivisionsIds(List<UUID> subdivisionsIds);
}
