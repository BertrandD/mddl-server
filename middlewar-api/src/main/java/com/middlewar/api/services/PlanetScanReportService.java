package com.middlewar.api.services;

import com.middlewar.api.dao.PlanetScanReportDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;

/**
 * @author Leboc Philippe.
 */
public interface PlanetScanReportService extends DefaultService<PlanetScanReport, PlanetScanReportDao> {
    PlanetScanReport create(Player owner, Base baseSrc, Planet planet);
}
