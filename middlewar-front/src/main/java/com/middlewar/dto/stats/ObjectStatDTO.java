package com.middlewar.dto.stats;

import com.middlewar.dto.holder.StatHolderDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ObjectStatDTO {
    private Map<String, List<StatHolderDTO>> stats;
}
