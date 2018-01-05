package com.middlewar.api.manager;

import com.middlewar.core.exceptions.BaseNotFoundException;
import com.middlewar.core.exceptions.BaseNotOwnedException;
import com.middlewar.core.exceptions.SpyReportCreationException;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.report.SpyReport;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface ReportManager {

    List<Report> getAllReportsOfCurrentPlayer(@NotNull Player player);

    /**
     * @param player owner of the spy report
     * @param baseId source of the spy mission
     * @param target target of the spy mission
     * @return the spy report
     * @throws BaseNotFoundException      if one of the given base id is not found
     * @throws BaseNotOwnedException      if the source base is now owned by the given player
     * @throws SpyReportCreationException if something went wrong
     */
    SpyReport spy(@NotNull Player player, int baseId, int target);
}
