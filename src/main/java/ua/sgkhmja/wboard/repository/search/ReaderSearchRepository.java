package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.Reader;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reader entity.
 */
public interface ReaderSearchRepository extends ElasticsearchRepository<Reader, Long> {
}
