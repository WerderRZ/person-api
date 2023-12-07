package com.werdersoft.personapi.subdivision;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubdivisionRepository extends CrudRepository<Subdivision, UUID> {
}
