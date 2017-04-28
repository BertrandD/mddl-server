package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Player;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

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
    private String id;

    private long date;

    @DBRef
    @JsonBackReference
    private Player owner;

    private HashMap<ReportCategory, List<ReportEntry>> entries;

    private ReportStatus reportStatus;

    public abstract ReportType getType();

    public Report(Player owner, ReportStatus reportStatus) {
        setId(new ObjectId().toString());
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
