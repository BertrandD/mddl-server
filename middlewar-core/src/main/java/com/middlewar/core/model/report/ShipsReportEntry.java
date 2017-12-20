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
public class ShipsReportEntry extends ReportEntry {

    long amount;

    public ShipsReportEntry(String name, long amount) {
        super(name);
        setAmount(amount);
    }
}
