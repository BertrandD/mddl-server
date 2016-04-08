package com.gameserver.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
public class DefaultController {

    @RequestMapping(value = "/", produces = "application/json")
    public String index()
    {
        return "index";
    }

    @RequestMapping(value = "/heading", produces = "application/json")
    public void headInformations()
    {

    }

}
