package com.middlewar.controllers.statics;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.CommonItem;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/static/item", produces = "application/json")
public class ItemController {

    @RequestMapping(method = GET)
    public Response findAll(@AuthenticationPrincipal Account pAccount) {
        final Map<String, List<? extends GameItem>> all = new HashMap<>();
        final ItemData itemData = ItemData.getInstance();
        all.put("COMMON", itemData.getCommonItems());
        all.put("CARGO", itemData.getCargos());
        all.put("ENGINE", itemData.getEngines());
        all.put("MODULE", itemData.getModules());
        all.put("RESOURCE", itemData.getResources());
        all.put("STRUCTURE", itemData.getStructures());
        all.put("WEAPON", itemData.getWeapons());
        return new Response(all);
    }

    @RequestMapping(value = "/common", method = GET)
    public List<CommonItem> findAllCommonItems() {
        return ItemData.getInstance().getCommonItems();
    }

    @RequestMapping(value = "/cargo", method = GET)
    public List<Cargo> findAllCargos() {
        return ItemData.getInstance().getCargos();
    }

    @RequestMapping(value = "/engine", method = GET)
    public List<Engine> findAllEngine() {
        return ItemData.getInstance().getEngines();
    }

    @RequestMapping(value = "/module", method = GET)
    public List<Module> findAllModules() {
        return ItemData.getInstance().getModules();
    }

    @RequestMapping(value = "/structure", method = GET)
    public List<Structure> findAllStructures() {
        return ItemData.getInstance().getStructures();
    }

    @RequestMapping(value = "/weapon", method = GET)
    public List<Weapon> findAllWeapons() {
        return ItemData.getInstance().getWeapons();
    }

    @RequestMapping(value = "/common/{id}", method = GET)
    public CommonItem findCommonItem(@PathVariable("id") String id) {
        return ItemData.getInstance().getCommonItem(id);
    }

    @RequestMapping(value = "/cargo/{id}", method = GET)
    public Cargo findCargo(@PathVariable("id") String id) {
        return ItemData.getInstance().getCargo(id);
    }

    @RequestMapping(value = "/engine/{id}", method = GET)
    public Engine findEngine(@PathVariable("id") String id) {
        return ItemData.getInstance().getEngine(id);
    }

    @RequestMapping(value = "/module/{id}", method = GET)
    public Module findModule(@PathVariable("id") String id) {
        return ItemData.getInstance().getModule(id);
    }

    @RequestMapping(value = "/structure/{id}", method = GET)
    public Structure findStructure(@PathVariable("id") String id) {
        return ItemData.getInstance().getStructure(id);
    }

    @RequestMapping(value = "/weapon/{id}", method = GET)
    public Weapon findWeapon(@PathVariable("id") String id) {
        return ItemData.getInstance().getWeapon(id);
    }
}
