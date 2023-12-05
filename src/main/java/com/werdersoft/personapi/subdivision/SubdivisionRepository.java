package com.werdersoft.personapi.subdivision;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubdivisionRepository extends CrudRepository<Subdivision, Long> {
}
