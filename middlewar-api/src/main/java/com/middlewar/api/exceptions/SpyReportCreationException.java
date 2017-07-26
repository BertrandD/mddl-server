package com.middlewar.api.exceptions;

import com.middlewar.api.util.response.SystemMessageId;

/**
 * @author Bertrand
 */
public class SpyReportCreationException extends ApiException {
    public SpyReportCreationException() {
        super(SystemMessageId.CANNOT_CREATE_SPY_REPORT);
    }
}
