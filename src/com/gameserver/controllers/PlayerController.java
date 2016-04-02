package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Player;
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
@RequestMapping(value = "/player", produces = "application/json")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @JsonView(View.Standard.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<Player> players(){
        return playerService.findAll();
    }

    @JsonView(View.PlayerBases.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Player player(@PathVariable("id") String id){
        return playerService.findOne(id);
    }

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.POST)
    public Player create(@RequestParam(value = "name") String name){
        return playerService.create(name);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam("id") String id){
        playerService.delete(id);
    }
}
