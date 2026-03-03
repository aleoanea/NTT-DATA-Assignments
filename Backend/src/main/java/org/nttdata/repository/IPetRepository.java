package org.nttdata.repository;

import org.nttdata.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPetRepository extends JpaRepository<Pet, Long> {

}
