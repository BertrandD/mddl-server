package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.SpyReport;

import javax.validation.constraints.NotNull;

/**
 * @author LEBOC Philippe
 */
public interface SpyReportService extends CrudService<SpyReport, Integer> {
    SpyReport create(@NotNull Player owner, @NotNull Base baseSrc, @NotNull Base baseTarget);
}
