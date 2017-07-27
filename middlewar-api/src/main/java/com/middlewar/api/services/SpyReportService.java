package com.middlewar.api.services;

import com.middlewar.api.dao.SpyReportDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.SpyReport;

/**
 * @author Leboc Philippe.
 */
public interface SpyReportService extends DefaultService<SpyReport, SpyReportDao> {
    SpyReport create(Player owner, Base baseSrc, Base baseTarget);
}
