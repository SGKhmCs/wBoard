package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.ReaderTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReaderTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReaderToolsRepository extends JpaRepository<ReaderTools,Long> {

}
