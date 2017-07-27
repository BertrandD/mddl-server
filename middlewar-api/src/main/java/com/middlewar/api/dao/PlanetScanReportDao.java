package com.middlewar.api.dao;

import com.middlewar.core.model.report.PlanetScanReport;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Leboc Philippe.
 */
public interface PlanetScanReportDao extends JpaRepository<PlanetScanReport, Long> {
}
