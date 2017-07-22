package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Player;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
public abstract class Report implements Comparable<Report> {

    @Id
    @GeneratedValue
    private String id;

    private long date;

    @ManyToOne
    @JsonBackReference
    private Player owner;

    private HashMap<ReportCategory, List<ReportEntry>> entries;

    private ReportStatus reportStatus;

    public abstract ReportType getType();

    public Report(Player owner, ReportStatus reportStatus) {
        setDate(TimeUtil.getCurrentTime());
        setOwner(owner);
        setEntries(new HashMap<>());
        setReportStatus(reportStatus);
    }

    public void addEntry(String name, Object value, ReportCategory category) {
        ReportEntry reportEntry = new ReportEntry(name, value);
        if (!entries.containsKey(category)) {
            entries.put(category, new ArrayList<>());
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
