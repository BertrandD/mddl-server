package com.gameserver.controllers.ship;

import com.auth.Account;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.vehicles.Ship;
import com.gameserver.services.PlayerService;
import com.util.response.JsonResponse;
import com.util.response.JsonResponseType;
import com.util.response.SystemMessageId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/ship", produces = "application/json")
public class ShipController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.POST)
    public JsonResponse create(@AuthenticationPrincipal Account pAccount,
                               @RequestParam(value = "structureId") String structure,
                               @RequestParam(value = "attachments") List<String> ids) {

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        final Base base = player.getCurrentBase();
        if(base == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.BASE_NOT_FOUND);
        base.initializeStats();

        if(ItemData.getInstance().getStructure(structure) == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        boolean faillure = false;
        for (int i = 0; i < ids.size() && !faillure; i++)
            if(ItemData.getInstance().getTemplate(ids.get(i)) == null) faillure = true;

        if(faillure) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.ITEM_NOT_FOUND);

        // TODO: check if base has items.

        final Ship ship = new Ship(base, structure, 1);
        ship.setCargoIds(ids.stream().filter(k -> k.startsWith("cargo_")).collect(Collectors.toList()));
        ship.setEngineIds(ids.stream().filter(k -> k.startsWith("engine_")).collect(Collectors.toList()));
        ship.setModuleIds(ids.stream().filter(k -> k.startsWith("module_")).collect(Collectors.toList()));
        ship.setWeaponIds(ids.stream().filter(k -> k.startsWith("weapon_")).collect(Collectors.toList()));
        return new JsonResponse(JsonResponseType.ERROR);
    }

}
