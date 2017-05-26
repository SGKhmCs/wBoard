package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.WriterTools;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WriterTools entity.
 */
public interface WriterToolsSearchRepository extends ElasticsearchRepository<WriterTools, Long> {
}
