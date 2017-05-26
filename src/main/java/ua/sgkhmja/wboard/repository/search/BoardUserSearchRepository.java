package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.BoardUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BoardUser entity.
 */
public interface BoardUserSearchRepository extends ElasticsearchRepository<BoardUser, Long> {
}
