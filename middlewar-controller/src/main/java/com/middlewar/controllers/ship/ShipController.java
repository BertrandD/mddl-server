package com.middlewar.controllers.ship;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.request.ShipCreationRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/ship", produces = "application/json")
public class ShipController {

    @RequestMapping(method = POST)
    public Response create(@AuthenticationPrincipal Account account, @RequestBody ShipCreationRequest request) {
        return new Response();
    }
}
