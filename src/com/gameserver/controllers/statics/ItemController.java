package com.gameserver.controllers.statics;

import com.auth.Account;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.CommonItem;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import com.util.data.json.Response.JsonResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
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
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount){
        final HashMap<String, List<? extends GameItem>> all = new HashMap<>();

        ItemData itemData = ItemData.getInstance();

        all.put("COMMON", itemData.getCommonItems(pAccount.getLang()));
        all.put("CARGO", itemData.getCargos(pAccount.getLang()));
        all.put("ENGINE", itemData.getEngines(pAccount.getLang()));
        all.put("MODULE", itemData.getModules(pAccount.getLang()));
        all.put("RESOURCE", itemData.getResources(pAccount.getLang()));
        all.put("STRUCTURE", itemData.getStructures(pAccount.getLang()));
        all.put("WEAPON", itemData.getWeapons(pAccount.getLang()));

        return new JsonResponse(all);
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

    @RequestMapping(value = "/common/{id}", method = RequestMethod.GET)
    public CommonItem findCommonItem(@PathVariable("id") String id){
        return ItemData.getInstance().getCommonItem(id);
    }

    @RequestMapping(value = "/cargo/{id}", method = RequestMethod.GET)
    public Cargo findCargo(@PathVariable("id") String id){
        return ItemData.getInstance().getCargo(id);
    }

    @RequestMapping(value = "/engine/{id}", method = RequestMethod.GET)
    public Engine findEngine(@PathVariable("id") String id){
        return ItemData.getInstance().getEngine(id);
    }

    @RequestMapping(value = "/module/{id}", method = RequestMethod.GET)
    public Module findModule(@PathVariable("id") String id){
        return ItemData.getInstance().getModule(id);
    }

    @RequestMapping(value = "/structure/{id}", method = RequestMethod.GET)
    public Structure findStructure(@PathVariable("id") String id){
        return ItemData.getInstance().getStructure(id);
    }

    @RequestMapping(value = "/weapon/{id}", method = RequestMethod.GET)
    public Weapon findWeapon(@PathVariable("id") String id){
        return ItemData.getInstance().getWeapon(id);
    }
}
