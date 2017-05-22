package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.Permission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Permission entity.
 */
public interface PermissionSearchRepository extends ElasticsearchRepository<Permission, Long> {
}
