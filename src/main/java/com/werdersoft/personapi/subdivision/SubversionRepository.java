package com.werdersoft.personapi.subdivision;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubversionRepository extends CrudRepository<Subdivision, Long> {
}
