package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.Reader;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Reader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReaderRepository extends JpaRepository<Reader,Long> {

}
