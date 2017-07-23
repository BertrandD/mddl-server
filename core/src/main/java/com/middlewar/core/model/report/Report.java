package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Player;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Report implements Comparable<Report> {

    @Id
    @GeneratedValue
    private String id;

    private long date;

    @ManyToOne
    @JsonBackReference
    private Player owner;

    @MapKeyEnumerated
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<ReportCategory, ReportEntryList> entries;

    @Enumerated
    private ReportStatus reportStatus;

    public abstract ReportType getType();

    public Report(Player owner, ReportStatus reportStatus) {
        setDate(TimeUtil.getCurrentTime());
        setOwner(owner);
        setEntries(new HashMap<>());
        setReportStatus(reportStatus);
    }

    public void addEntry(ReportEntry reportEntry, ReportCategory category) {
        if (!entries.containsKey(category)) {
            entries.put(category, new ReportEntryList());
        }
        entries.get(category).add(reportEntry);
    }

    @Override
    public int compareTo(Report o) {
        if (getDate() < o.getDate()) {
            return -1;
        } else if (getDate() == o.getDate()) {
            return 0;
        }
        return -1;
    }
}
