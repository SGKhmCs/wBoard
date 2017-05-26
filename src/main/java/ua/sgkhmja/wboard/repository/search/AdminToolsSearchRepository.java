package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.AdminTools;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AdminTools entity.
 */
public interface AdminToolsSearchRepository extends ElasticsearchRepository<AdminTools, Long> {
}
