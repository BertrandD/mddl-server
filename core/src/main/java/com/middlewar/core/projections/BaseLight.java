package com.middlewar.core.projections;

import com.middlewar.core.model.Base;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bertrand.
 * TODO: rename and move to holder
 */
@Data
@NoArgsConstructor
public class BaseLight {
    private String id;
    private String name;
    private PlayerLight owner;

    public BaseLight(Base base) {
        setId(base.getId());
        setName(base.getName());
        setOwner(new PlayerLight(base.getOwner()));
    }
}
