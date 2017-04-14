package com.middlewar.core.holders;

import com.middlewar.core.model.Base;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bertrand.
 * TODO: rename and move to holder
 */
@Data
@NoArgsConstructor
public class BaseHolder {
    private String id;
    private String name;
    private PlayerHolder owner;

    public BaseHolder(Base base) {
        setId(base.getId());
        setName(base.getName());
        setOwner(new PlayerHolder(base.getOwner()));
    }
}
