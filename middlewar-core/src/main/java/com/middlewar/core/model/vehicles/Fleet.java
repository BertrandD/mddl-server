package com.middlewar.core.model.vehicles;

import com.middlewar.core.enums.VehicleMission;
import com.middlewar.core.model.commons.Coordinates;
import com.middlewar.core.model.stats.ObjectStat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@Table(name = "fleets")
public class Fleet {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne
    private Coordinates departure;

    @NotNull
    @ManyToOne
    private Coordinates arrival;

    @NotNull
    private VehicleMission mission;

    @Transient
    private ObjectStat stats;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ship> ships;

    public Fleet() {
        setShips(emptyList());
        setStats(new ObjectStat());
    }

    public Fleet(Coordinates arrival, Coordinates departure, VehicleMission mission) {
        setShips(emptyList());
        setStats(new ObjectStat());
        setDeparture(departure);
        setArrival(arrival);
        setMission(mission);
    }

/*
    public void initializeStats() {
        final ObjectStat stats = getStats();

        // Unlock stats
        stats.unlock(Stats.FLEET_HEALTH);
        stats.unlock(Stats.FLEET_MAX_HEALTH);
        stats.unlock(Stats.FLEET_SHIELD);
        stats.unlock(Stats.FLEET_MAX_SHIELD);
        stats.unlock(Stats.FLEET_DAMAGE);

        // Apply specific ship stats and modules effects
        getShips().forEach(ship -> {
            stats.add(Stats.FLEET_DAMAGE, ship.getDamage(), StatOp.DIFF);
            stats.add(Stats.FLEET_CARGO, ship.getMaxStorableVolume(), StatOp.DIFF);

            ship.getCargos().forEach(cargo -> cargo.handleEffect(stats));
            ship.getEngines().forEach(engine -> engine.handleEffect(stats));
            ship.getWeapons().forEach(weapon -> weapon.handleEffect(stats));
            // Module must be last
            ship.getModules().forEach(module -> module.handleEffect(stats));
        });

    }
*/
}
