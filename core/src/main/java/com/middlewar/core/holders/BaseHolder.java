package com.middlewar.core.holders;

import com.middlewar.core.model.Base;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author bertrand.
 * TODO: rename and move to holder
 */
@Data
@NoArgsConstructor
@Entity
public class BaseHolder {
    @Id
    private String id;
    private String name;
    @ManyToOne
    private PlayerHolder owner;

    public BaseHolder(Base base) {
        setId(base.getId());
        setName(base.getName());
        setOwner(new PlayerHolder(base.getOwner()));
    }
}
