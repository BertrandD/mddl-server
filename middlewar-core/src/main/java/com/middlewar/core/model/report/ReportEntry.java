package com.middlewar.core.model.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author bertrand.
 */
@Getter
@Setter
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
