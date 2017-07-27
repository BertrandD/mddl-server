package com.middlewar.api.manager;

import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.SpyReportCreationException;
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
public class ReportManager {

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private SpyReportServiceImpl spyReportServiceImpl;

    public List<Report> getAllReportsOfCurrentPlayer(Player player) throws NoPlayerConnectedException, PlayerNotFoundException {
        player.getCurrentBase().getReports().sort(Collections.reverseOrder());
        return player.getCurrentBase().getReports();

    }

    /**
     * @param player owner of the spy report
     * @param baseId source of the spy mission
     * @param target target of the spy mission
     * @return the spy report
     * @throws BaseNotFoundException      if one of the given base id is not found
     * @throws BaseNotOwnedException      if the source base is now owned by the given player
     * @throws SpyReportCreationException if something went wrong
     */
    public SpyReport spy(Player player, long baseId, long target) throws BaseNotFoundException, BaseNotOwnedException, SpyReportCreationException {
        final Base base = baseManager.getOwnedBase(baseId, player);
        final Base baseTarget = baseManager.getBase(target);

        final SpyReport report = spyReportServiceImpl.create(player, base, baseTarget);
        if (report == null) throw new SpyReportCreationException();

        return report;
    }
}
