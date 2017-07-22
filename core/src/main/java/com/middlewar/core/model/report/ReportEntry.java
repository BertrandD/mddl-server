package com.middlewar.core.model.report;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author bertrand.
 */
@Data
@Entity
@NoArgsConstructor
public class ReportEntry {

    @Id
    @GeneratedValue
    private String id;
    private String name;
//    @Transient
//    private Object value; // TODO make it not ignored (Transient) /!\ IMPORTANT /!\

    public ReportEntry(String name, Object value) {
        this.name = name;
//        this.value = value;
    }
}
