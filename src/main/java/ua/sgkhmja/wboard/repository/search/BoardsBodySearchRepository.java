package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.BoardsBody;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BoardsBody entity.
 */
public interface BoardsBodySearchRepository extends ElasticsearchRepository<BoardsBody, Long> {
}
