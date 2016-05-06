package com.gameserver.services;

import com.gameserver.model.buildings.Mine;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.repository.BaseRepository;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseService {

    @Autowired
    private BaseRepository repository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PlayerService playerService;

    private static final String MINE_METAL = "mine_metal";
    private static final String METAL = "metal";

    public Base findOne(String id){
        final Base base = repository.findOne(id);
        final BuildingInstance mineMetal = base.getBuildings().stream().filter(k->k.getBuildingId().equals(MINE_METAL)).findFirst().orElse(null);
        if(mineMetal != null && mineMetal.getCurrentLevel() > 0)
        {
            final ResourceInventory resources = base.getResources();
            ItemInstance metal = resources.getItems().stream().filter(k->k.getTemplateId().equals(METAL)).findFirst().orElse(null);
            if(metal != null)
            {
                final long now = System.currentTimeMillis();
                final Mine template = (Mine) mineMetal.getTemplate();
                final long amount = (long) ((((float)template.getProduction(mineMetal.getCurrentLevel()) / 3600)) * ((now - resources.getLastRefresh()) / 1000));

                if(amount > 0)
                {
                    inventoryService.addItem(metal, amount);
                }
            }
        }
        return base;
    }

    public Collection<Base> findAll(){
        return repository.findAll();
    }

    public Base create(String name, Player player){
        final Base base = new Base(name, player);
        base.setResources(inventoryService.createResourceInventory(base));
        base.setCommons(inventoryService.createBaseInventory(base));
        base.setShipItems(inventoryService.createBaseInventory(base));

        player.addBase(base);
        player.setCurrentBase(base);
        playerService.update(player);

        return repository.save(base);
    }

    public void update(Base b) { repository.save(b); }

    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }
}
