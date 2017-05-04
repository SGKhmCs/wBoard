package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.PermissionType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PermissionType entity.
 */
public interface PermissionTypeSearchRepository extends ElasticsearchRepository<PermissionType, Long> {
}
