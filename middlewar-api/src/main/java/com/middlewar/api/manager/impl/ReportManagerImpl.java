package com.middlewar.api.manager.impl;

import com.middlewar.api.exceptions.SpyReportCreationException;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.services.impl.SpyReportServiceImpl;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.report.SpyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Bertrand
 */
@Service
public class ReportManagerImpl implements ReportManager {

    @Autowired
    private BaseManagerImpl baseManagerImpl;

    @Autowired
    private SpyReportServiceImpl spyReportService;

    public List<Report> getAllReportsOfCurrentPlayer(Player player) {
        player.getCurrentBase().getReports().sort(Collections.reverseOrder());
        return player.getCurrentBase().getReports();
    }

    public SpyReport spy(Player player, long baseId, long target) {
        final Base base = baseManagerImpl.getOwnedBase(baseId, player);
        final Base baseTarget = baseManagerImpl.getBase(target);

        final SpyReport report = spyReportService.create(player, base, baseTarget);
        if (report == null) throw new SpyReportCreationException();

        return report;
    }
}
