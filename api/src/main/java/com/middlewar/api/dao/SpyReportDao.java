package com.middlewar.api.dao;

import com.middlewar.core.model.report.SpyReport;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface SpyReportDao extends MongoRepository<SpyReport, String> {
}
