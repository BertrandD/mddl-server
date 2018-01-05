package com.middlewar.controllers.buildings;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.FactoryManager;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.request.FactoryModuleCreationRequest;
import com.middlewar.request.FactoryStructureCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/factory", produces = "application/json")
public class ItemFactoryController {

    @Autowired
    private FactoryManager factoryManager;

    @RequestMapping(value = "/module", method = POST)
    public Response createModule(@AuthenticationPrincipal Account account, @RequestBody FactoryModuleCreationRequest request) {
        return new Response(factoryManager.createModule(account.getCurrentPlayer(), request.getBaseId(), request.getFactoryId(), request.getTemplateId()));
    }

    @RequestMapping(value = "/structure", method = POST)
    public Response createStructure(@AuthenticationPrincipal Account account, @RequestBody FactoryStructureCreationRequest request) {
        return new Response(factoryManager.createStructure(account.getCurrentPlayer(), request.getBaseId(), request.getFactoryId(), request.getTemplateId()));
    }
}
