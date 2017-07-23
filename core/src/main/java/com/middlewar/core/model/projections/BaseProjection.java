package com.middlewar.core.model.projections;

import com.middlewar.core.model.Base;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author Bertrand
 */
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public class BaseProjection {
    @Id
    @GeneratedValue
    private long id;
    private long baseId;
    private String baseName;
    private long baseOwnerId;
    private String baseOwnerName;

    // TODO : add more info for report

    public BaseProjection(Base base) {
        setBaseId(base.getId());
        setBaseName(base.getName());
        setBaseOwnerId(base.getOwner().getId());
        setBaseOwnerName(base.getOwner().getName());
    }
}
