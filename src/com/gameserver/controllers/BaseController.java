package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.services.BaseService;
import com.gameserver.services.PlayerService;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/base", produces = "application/json")
public class BaseController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerService playerService;

    @JsonView(View.Base_Onwer.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<Base> findAll(){
        return baseService.findAll();
    }

    @JsonView(View.Base_OwnerAndBuildings.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Base findOne(@PathVariable("id") String id){
        return baseService.findOne(id);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.POST)
    public Base create(@RequestParam(value = "name") String name, @RequestParam(value = "player") String player) {
        Player p = playerService.findOne(player);
        if(p == null) return null;
        Base base = baseService.create(name, p);
        p.addBase(base);
        playerService.update(p);
        return base;
    }
}
