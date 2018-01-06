package com.middlewar.core.model.instances;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.items.Item;
import com.middlewar.core.model.items.SlotItem;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.stats.StatCalculator;
import com.middlewar.core.model.stats.Stats;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
public class RecipeInstance {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToOne
    private Player owner;

    @Transient
    private Structure structure;

    @Transient
    private List<SlotItem> components;

    public RecipeInstance() {
        setComponents(new ArrayList<>());
    }

    public RecipeInstance(long id, String name, Player owner, Structure structure, List<SlotItem> components) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.structure = structure;
        this.components = components;
    }

    private double calcStat(Stats stats) {
        StatCalculator damage = new StatCalculator(stats);

        for (Item item : components) damage.add(item.getStats().get(stats));

        return damage.getValue();
    }

    public double calcPower() {
        return calcStat(Stats.POWER);
    }

    public double calcDamage() {
        return calcStat(Stats.DAMAGE);
    }

    public double calcCargo() {
        return calcStat(Stats.CARGO);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof RecipeInstance && ((RecipeInstance) o).id == id;
    }
}
