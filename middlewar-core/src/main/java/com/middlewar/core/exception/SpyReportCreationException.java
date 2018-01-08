package com.middlewar.core.exception;

import com.middlewar.core.utils.SystemMessageId;

/**
 * @author Bertrand
 */
public class SpyReportCreationException extends ApiException {
    public SpyReportCreationException() {
        super(SystemMessageId.CANNOT_CREATE_SPY_REPORT);
    }
}
