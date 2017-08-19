package com.middlewar.controllers.statics;

import com.middlewar.api.util.response.Response;
import com.middlewar.client.Route;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.CommonItem;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class ItemController {

    @RequestMapping(value = Route.STATIC_ITEM_ALL, method = RequestMethod.GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount) {
        final HashMap<String, List<? extends GameItem>> all = new HashMap<>();

        ItemData itemData = ItemData.getInstance();

        all.put("COMMON", itemData.getCommonItems(pAccount.getLang()));
        all.put("CARGO", itemData.getCargos(pAccount.getLang()));
        all.put("ENGINE", itemData.getEngines(pAccount.getLang()));
        all.put("MODULE", itemData.getModules(pAccount.getLang()));
        all.put("RESOURCE", itemData.getResources(pAccount.getLang()));
        all.put("STRUCTURE", itemData.getStructures(pAccount.getLang()));
        all.put("WEAPON", itemData.getWeapons(pAccount.getLang()));

        return new Response<>(all);
    }

    @RequestMapping(value = Route.STATIC_ITEM_COMMON, method = RequestMethod.GET)
    public List<CommonItem> findAllCommonItems() {
        return ItemData.getInstance().getCommonItems();
    }

    @RequestMapping(value = Route.STATIC_ITEM_CARGO, method = RequestMethod.GET)
    public List<Cargo> findAllCargos() {
        return ItemData.getInstance().getCargos();
    }

    @RequestMapping(value = Route.STATIC_ITEM_ENGINE, method = RequestMethod.GET)
    public List<Engine> findAllEngine() {
        return ItemData.getInstance().getEngines();
    }

    @RequestMapping(value = Route.STATIC_ITEM_MODULE, method = RequestMethod.GET)
    public List<Module> findAllModules() {
        return ItemData.getInstance().getModules();
    }

    @RequestMapping(value = Route.STATIC_ITEM_STRUCTURE, method = RequestMethod.GET)
    public List<Structure> findAllStructures() {
        return ItemData.getInstance().getStructures();
    }

    @RequestMapping(value = Route.STATIC_ITEM_WEAPON, method = RequestMethod.GET)
    public List<Weapon> findAllWeapons() {
        return ItemData.getInstance().getWeapons();
    }

    @RequestMapping(value = Route.STATIC_ITEM_COMMON_ONE, method = RequestMethod.GET)
    public CommonItem findCommonItem(@PathVariable("id") String id) {
        return ItemData.getInstance().getCommonItem(id);
    }

    @RequestMapping(value = Route.STATIC_ITEM_CARGO_ONE, method = RequestMethod.GET)
    public Cargo findCargo(@PathVariable("id") String id) {
        return ItemData.getInstance().getCargo(id);
    }

    @RequestMapping(value = Route.STATIC_ITEM_ENGINE_ONE, method = RequestMethod.GET)
    public Engine findEngine(@PathVariable("id") String id) {
        return ItemData.getInstance().getEngine(id);
    }

    @RequestMapping(value = Route.STATIC_ITEM_MODULE_ONE, method = RequestMethod.GET)
    public Module findModule(@PathVariable("id") String id) {
        return ItemData.getInstance().getModule(id);
    }

    @RequestMapping(value = Route.STATIC_ITEM_STRUCTURE_ONE, method = RequestMethod.GET)
    public Structure findStructure(@PathVariable("id") String id) {
        return ItemData.getInstance().getStructure(id);
    }

    @RequestMapping(value = Route.STATIC_ITEM_WEAPON_ONE, method = RequestMethod.GET)
    public Weapon findWeapon(@PathVariable("id") String id) {
        return ItemData.getInstance().getWeapon(id);
    }
}
