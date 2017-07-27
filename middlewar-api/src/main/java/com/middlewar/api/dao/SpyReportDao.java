package com.middlewar.api.dao;

import com.middlewar.core.model.report.SpyReport;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Leboc Philippe.
 */
public interface SpyReportDao extends JpaRepository<SpyReport, Long> {
}
