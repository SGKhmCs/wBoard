package ua.sgkhmja.wboard.repository;

import ua.sgkhmja.wboard.domain.WriterTools;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WriterTools entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WriterToolsRepository extends JpaRepository<WriterTools,Long> {

}
