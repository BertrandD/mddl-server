package com.middlewar.core.model.report;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.projections.BaseReportProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author bertrand.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class BaseReportEntry extends ReportEntry {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private BaseReportProjection base;

    public BaseReportEntry(Base base) {
        super(base.getName());
        setBase(new BaseReportProjection(base));
    }
}
