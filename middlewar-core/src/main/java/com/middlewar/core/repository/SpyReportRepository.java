package com.middlewar.core.repository;

import com.middlewar.core.model.report.SpyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LEBOC Philippe
 */
@Repository
public interface SpyReportRepository extends JpaRepository<SpyReport, Integer> {
}
