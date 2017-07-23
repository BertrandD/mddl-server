package com.middlewar.core.model.report;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author bertrand.
 */
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class ReportEntry {

    @Id
    @GeneratedValue
    private long id;
    private String name;

    public ReportEntry(String name) {
        this.name = name;
    }
}
