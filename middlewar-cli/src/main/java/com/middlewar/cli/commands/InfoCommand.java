package com.middlewar.cli.commands;

import com.middlewar.cli.GameContext;
import com.middlewar.client.BaseClient;
import com.middlewar.client.PlayerClient;
import com.middlewar.client.StaticClient;
import com.middlewar.dto.AccountDTO;
import com.middlewar.dto.BaseDTO;
import com.middlewar.dto.BuildingDTO;
import com.middlewar.dto.PlayerDTO;
import com.middlewar.dto.commons.RequirementDTO;
import com.middlewar.dto.holder.BuildingHolderDTO;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Bertrand
 */
public class InfoCommand extends Command {
    public InfoCommand() {
        super("info <account|player|base> | info buildings [all|BUILDING_ID [level]]", "Display info");
    }

    @Override
    public void exec() {
        if (getInput().length <= 1) {
            printUsage();
            return;
        }


        switch (getInput()[1]) {
            case "account":
                if (GameContext.getInstance().getAccount() == null) {
                    System.out.println("No account in context");
                    return;
                }
                AccountDTO account = GameContext.getInstance().getAccount();
                if (account == null) {
                    System.out.println("You are not logged in");
                    return;
                }

                System.out.println(account.toString());
                System.out.println("ID       : " + account.getId());
                System.out.println("Username : " + account.getUsername());
                break;
            case "player":
                if (GameContext.getInstance().getPlayer() == null) {
                    System.out.println("No player in context");
                    return;
                }

                GameContext.getInstance().setPlayer(PlayerClient.getPlayer(GameContext.getInstance().getPlayer().getId()));
                PlayerDTO player = GameContext.getInstance().getPlayer();
                if (player == null) {
                    System.out.println("You don't have any player. Let's create it !");
                    return;
                }
                System.out.println(player.toString());
                System.out.println("ID   : " + player.getId());
                System.out.println("Name : " + player.getName());
                break;
            case "base":
                if (GameContext.getInstance().getBase() == null) {
                    System.out.println("No base in context");
                }
                GameContext.getInstance().setBase(BaseClient.getBase(GameContext.getInstance().getBase().getId()));
                BaseDTO base = GameContext.getInstance().getBase();
                if (base == null) {
                    System.out.println("You don't have any base. Let's create it !");
                    return;
                }
                System.out.println(base.toString());
                System.out.println("ID   : " + base.getId());
                System.out.println("Name : " + base.getName());
                System.out.println("Resources : ");
                base.getResources().forEach(k -> System.out.println(k.getCount() + "/" + k.getAvailableCapacity() + " (" + k.getProdPerHour() + "/h)"));
                System.out.println("Buildings : ");
                base.getBuildings().forEach(k -> {
                    System.out.println("\t" + k.getBuildingId() + " (lvl " + k.getCurrentLevel() + ")");
                    if (k.getEndsAt() != -1) {
                        System.out.println("\t\t Upgrading to lvl " + (k.getCurrentLevel() + 1) + ". Time left : " + (k.getEndsAt() - System.currentTimeMillis()));
                    }
                    k.getModules().forEach(m -> System.out.println("\t\t" + m));
                });
                break;
            case "base.buildable":
                if (GameContext.getInstance().getBase() == null) {
                    System.out.println("No base in context");
                    return;
                }
                List<BuildingHolderDTO> buildings = BaseClient.getBuildables(GameContext.getInstance().getBase());
                buildings.forEach(k -> System.out.println(k.getTemplateId()));
                break;
            case "buildings":
                List<BuildingDTO> buildingDTOS = StaticClient.getBuildings();
                String option;
                try {
                    option = getInput()[2];
                } catch (IndexOutOfBoundsException e) {
                    option = "all";
                }
                switch (option) {
                    case "all":
                        System.out.println(buildingDTOS.size());
                        buildingDTOS.forEach(building -> {
                            System.out.println(building.getName());
                            System.out.println("\t" + building.getDescription());
                            System.out.println("\tRequirements;");
                            building.getRequirements().forEach((lvl, requirement) -> {
                                if (lvl > 3) return;
                                System.out.println("\t\t lvl " + lvl + ":");
                                System.out.println("\t\t  Items:");
                                requirement.getItems().forEach(k -> System.out.println("\t\t\t" + k.getCount() + " " + k.getId()));
                                System.out.println("\t\t  Buildings:");
                                requirement.getBuildings().forEach(k -> System.out.println("\t\t\t" + k.getTemplateId() + " (lvl " + k.getLevel() + ")"));
                            });
                        });
                        break;
                    default:
                        BuildingDTO building = buildingDTOS.stream().filter(k -> k.getId().equals(getInput()[2])).findFirst().orElse(null);
                        if (building == null) {
                            System.out.println("Building not found");
                            return;
                        }
                        System.out.println(building.getName());
                        System.out.println(building.getDescription());
                        int lvl;
                        try {
                            lvl = Integer.parseInt(getInput()[3]);
                        } catch (IndexOutOfBoundsException e) {
                            lvl = 1;
                        }
                        System.out.println("\tRequirements for lvl " + lvl);
                        RequirementDTO requirement = building.getRequirements().get(lvl);
                        System.out.println("\t\t  Items:");
                        requirement.getItems().forEach(k -> System.out.println("\t\t\t" + k.getCount() + " " + k.getId()));
                        System.out.println("\t\t  Buildings:");
                        requirement.getBuildings().forEach(k -> System.out.println("\t\t\t" + k.getTemplateId() + " (lvl " + k.getLevel() + ")"));
                }
                break;
            default:
                System.out.println("Unknown option " + getInput()[1]);
        }

    }
}
