package com.middlewar.core.repository;

import com.middlewar.core.model.report.PlanetScanReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Leboc Philippe.
 */
@Repository
public interface PlanetScanReportRepository extends JpaRepository<PlanetScanReport, Integer> {
}
