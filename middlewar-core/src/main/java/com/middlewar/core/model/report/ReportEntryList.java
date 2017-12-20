package com.middlewar.core.model.report;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bertrand
 */
@Entity
@Getter
@Setter
public class ReportEntryList {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportEntry> entries;

    public ReportEntryList() {
        entries = new ArrayList<>();
    }

    public ReportEntryList(List<ReportEntry> entries) {
        this.entries = entries;
    }

    public void add(ReportEntry reportEntry) {
        entries.add(reportEntry);
    }

    public ReportEntry get(int i) {
        return entries.get(i);
    }
}
