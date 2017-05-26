package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.ReaderTools;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReaderTools entity.
 */
public interface ReaderToolsSearchRepository extends ElasticsearchRepository<ReaderTools, Long> {
}
