package com.gameserver.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/", produces = "application/json")
public class DefaultController {


    public String index()
    {
        return "index";
    }
}
