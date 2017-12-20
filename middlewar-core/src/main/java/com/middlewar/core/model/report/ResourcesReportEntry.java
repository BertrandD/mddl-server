package com.middlewar.core.model.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author bertrand.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ResourcesReportEntry extends ReportEntry {

    double amount;

    public ResourcesReportEntry(String name, double amount) {
        super(name);
        setAmount(amount);
    }
}
