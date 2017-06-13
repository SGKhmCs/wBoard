package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.OwnerTools;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OwnerTools entity.
 */
public interface OwnerToolsSearchRepository extends ElasticsearchRepository<OwnerTools, Long> {
}
