package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class BuildingTemplateNotFoundException extends ApiException {
    public BuildingTemplateNotFoundException() {
        super(SystemMessageId.BUILDING_TEMPLATE_NOT_FOUND);
    }
}
