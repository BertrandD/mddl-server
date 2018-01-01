package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;

import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface PlanetScanReportService extends CrudService<PlanetScanReport, Integer> {
    PlanetScanReport create(@NotNull Player owner, @NotNull Base baseSrc, @NotNull Planet planet);
}
