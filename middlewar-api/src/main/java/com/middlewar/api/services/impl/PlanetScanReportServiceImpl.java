package com.middlewar.api.services.impl;

import com.middlewar.api.dao.PlanetScanReportDao;
import com.middlewar.api.dao.PlayerDao;
import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.BaseReportEntry;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bertrand.
 */
@Service
public class PlanetScanReportServiceImpl extends DefaultServiceImpl<PlanetScanReport, PlanetScanReportDao> implements PlanetScanReportService {

    @Autowired
    private PlanetScanReportDao planetScanReportDao;

    @Autowired
    private PlayerDao playerDao;

    @Override
    public PlanetScanReport create(Player owner, Base baseSrc, Planet planet) {

        // TODO : add logic for scan defense
        // TODO : send notification to scanned bases

        final PlanetScanReport report = new PlanetScanReport(owner, baseSrc, planet, ReportStatus.SUCCESS);

        owner.getCurrentBase().getReports().add(report);

        for (Base base : planet.getBases()) {
            report.addEntry(new BaseReportEntry(base), ReportCategory.BASES);
            owner.addPlanetScanned(planet, base);
        }

        planetScanReportDao.save(report);
        playerDao.save(owner);
        return report;
    }
}
