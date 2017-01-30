package com.middlewar.tests.services;

import com.middlewar.core.config.Config;
import com.middlewar.core.data.xml.SystemMessageData;

/**
 * @author bertrand.
 */
abstract class MiddlewarTest {
    MiddlewarTest() {
        Config.load();
        SystemMessageData.getInstance();
    }
}
