package com.middlewar.core.model.projections;

import com.middlewar.core.model.Base;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author Bertrand
 */
@Data
@Entity
@NoArgsConstructor
public class BaseReportProjection extends BaseProjection {

    public BaseReportProjection(Base base) {
        super(base);
    }
}
