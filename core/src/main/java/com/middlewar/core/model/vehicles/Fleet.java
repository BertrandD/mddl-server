package com.middlewar.core.model.vehicles;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.enums.VehicleMission;
import com.middlewar.core.model.commons.Coordinates;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.serializer.FleetSerializer;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = FleetSerializer.class)
public class Fleet {

    @Id
    private String id;
    @ManyToOne
    private Coordinates departure;
    @ManyToOne
    private Coordinates arrival;
    private VehicleMission mission;

    @Transient
    private ObjectStat stats;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ship> ships;

    public Fleet() {
        setShips(new ArrayList<>());
        setStats(new ObjectStat());
    }

    public Fleet(Coordinates arrival, Coordinates departure, VehicleMission mission) {
        setShips(new ArrayList<>());
        setStats(new ObjectStat());
        setDeparture(departure);
        setArrival(arrival);
        setMission(mission);
    }

    public void initializeStats() {
        final ObjectStat stats = getStats();

        // Unlock stats
        stats.addStat(Stats.FLEET_HEALTH);
        stats.addStat(Stats.FLEET_MAX_HEALTH);
        stats.addStat(Stats.FLEET_SHIELD);
        stats.addStat(Stats.FLEET_MAX_SHIELD);
        stats.addStat(Stats.FLEET_DAMAGE);

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

    public long getShipCount() {
        return getShips().stream().mapToLong(Ship::getCount).sum();
    }
}
