package com.gameserver.controllers;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.CommonItem;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Item;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/item_static", produces = "application/json")
public class ItemController {

    @RequestMapping(method = RequestMethod.GET)
    public HashMap<String, List<? extends Item>> findAll(){
        final HashMap<String, List<? extends Item>> all = new HashMap<>();
        all.put("structure", ItemData.getInstance().getStructures());
        all.put("cargo", ItemData.getInstance().getCargos());
        all.put("common", ItemData.getInstance().getCommonItems());
        return all;
    }

    @RequestMapping(value = "/common", method = RequestMethod.GET)
    public List<CommonItem> findAllCommonItems(){
        return ItemData.getInstance().getCommonItems();
    }

    @RequestMapping(value = "/cargo", method = RequestMethod.GET)
    public List<Cargo> findAllCargos(){
        return ItemData.getInstance().getCargos();
    }

    @RequestMapping(value = "/engine", method = RequestMethod.GET)
    public List<Engine> findAllEngine(){
        return ItemData.getInstance().getEngines();
    }

    @RequestMapping(value = "/module", method = RequestMethod.GET)
    public List<Module> findAllModules(){
        return ItemData.getInstance().getModules();
    }

    @RequestMapping(value = "/structure", method = RequestMethod.GET)
    public List<Structure> findAllStructures(){
        return ItemData.getInstance().getStructures();
    }

    @RequestMapping(value = "/weapon", method = RequestMethod.GET)
    public List<Weapon> findAllWeapons(){
        return ItemData.getInstance().getWeapons();
    }
}
