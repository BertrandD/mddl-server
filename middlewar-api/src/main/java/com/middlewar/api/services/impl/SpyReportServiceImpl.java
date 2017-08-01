package com.middlewar.api.services.impl;

import com.middlewar.api.dao.PlayerDao;
import com.middlewar.api.dao.SpyReportDao;
import com.middlewar.api.services.SpyReportService;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.report.ResourcesReportEntry;
import com.middlewar.core.model.report.ShipsReportEntry;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bertrand.
 */
@Service
public class SpyReportServiceImpl extends DefaultServiceImpl<SpyReport, SpyReportDao> implements SpyReportService {

    @Autowired
    private SpyReportDao spyReportDao;

    @Autowired
    private PlayerDao playerDao;

    @Override
    public SpyReport create(Player owner, Base baseSrc, Base baseTarget) {

        // TODO : add logic for spy defense

        final SpyReport report = new SpyReport(owner, baseSrc, baseTarget, ReportStatus.SUCCESS);

        owner.getCurrentBase().getReports().add(report);

        for (Ship ship : baseTarget.getShips()) {
            // TODO : instead of getting structure, we should get the Recipe
            // It would be fun if (depending of the spy level of the source Base, the report would
            // have less or more details on the recipe...
            report.addEntry(new ShipsReportEntry(ship.getStructure().getItemId(), ship.getCount()), ReportCategory.SHIPS);
        }

        for (Resource resource : baseTarget.getResources()) {
            report.addEntry(new ResourcesReportEntry(resource.getItem().getTemplateId(), resource.getCount()), ReportCategory.RESOURCES);
        }

        spyReportDao.save(report);
        playerDao.save(owner);
        return report;
    }
}