package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.client.BuildingClient;
import com.middlewar.dto.instances.BuildingInstanceDTO;

import java.util.Date;

/**
 * @author Bertrand
 */
public class BuildCommand extends Command {
    public BuildCommand() {
        super("build <templateId>", "Build the given building");
    }

    @Override
    public void exec() {
        String templateId = getParam("Template ID", 1);

        BuildingInstanceDTO buildingInstanceDTO = BuildingClient.create(GameContext.getInstance().getBase().getId(), templateId);

        Date end = new Date(buildingInstanceDTO.getEndsAt());
        System.out.println(buildingInstanceDTO.getBuildingId()+" with id " + buildingInstanceDTO.getId() + " will ends at " + end);
    }
}
