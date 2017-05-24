package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.Writer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Writer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WriterRepository extends JpaRepository<Writer,Long> {

}
