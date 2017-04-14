package com.middlewar.api.dao;

import com.middlewar.core.model.report.PlanetScanReport;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Leboc Philippe.
 */
public interface PlanetScanReportDao extends MongoRepository<PlanetScanReport, String> {
}
