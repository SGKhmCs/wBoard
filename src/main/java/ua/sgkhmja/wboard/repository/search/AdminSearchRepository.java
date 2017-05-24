package ua.sgkhmja.wboard.repository.search;

import ua.sgkhmja.wboard.domain.Admin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Admin entity.
 */
public interface AdminSearchRepository extends ElasticsearchRepository<Admin, Long> {
}
