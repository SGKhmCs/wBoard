package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.Board;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Board entity.
 */
public interface BoardSearchRepository extends ElasticsearchRepository<Board, Long> {
}
