package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.Writer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Writer entity.
 */
public interface WriterSearchRepository extends ElasticsearchRepository<Writer, Long> {
}
