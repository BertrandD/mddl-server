package com.middlewar.dto.instances;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.items.GameItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingInstanceDto {

    private int id;
    private String buildingId;
    private int currentLevel;
    private long endsAt;
    private long startedAt;
    private int baseId;
    private List<String> modules;

    public BuildingInstanceDto(final BuildingInstance instance) {
        this(
            instance.getId(),
            instance.getTemplateId(),
            instance.getCurrentLevel(),
            instance.getEndsAt(),
            instance.getStartedAt(),
            instance.getBase().getId(),
            instance.getModules().stream().map(GameItem::getItemId).collect(toList())
        );
    }
}
