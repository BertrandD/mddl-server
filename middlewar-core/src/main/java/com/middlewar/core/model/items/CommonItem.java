package com.middlewar.core.model.items;

import com.middlewar.core.model.commons.StatsSet;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public class CommonItem extends GameItem {
    public CommonItem(StatsSet set) {
        super(set);
    }
}
