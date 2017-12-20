package com.middlewar.api.manager;

import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.SpyReportCreationException;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.report.SpyReport;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface ReportManager {

    /**
     *
     * @param player
     * @return
     */
    List<Report> getAllReportsOfCurrentPlayer(Player player);

    /**
     * @param player owner of the spy report
     * @param baseId source of the spy mission
     * @param target target of the spy mission
     * @return the spy report
     * @throws BaseNotFoundException      if one of the given base id is not found
     * @throws BaseNotOwnedException      if the source base is now owned by the given player
     * @throws SpyReportCreationException if something went wrong
     */
    SpyReport spy(Player player, long baseId, long target);
}
