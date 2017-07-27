package com.middlewar.core.utils;

import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author LEBOC Philippe
 */
public class Evaluator {

    private final Logger LOGGER = Logger.getLogger(getClass().getSimpleName());
    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ScriptEngine engine = manager.getEngineByName("js");

    protected Evaluator() {
    }

    public static Evaluator getInstance() {
        return SingletonHolder._instance;
    }

    public Object eval(String expression) {
        Object result;
        try {
            result = engine.eval(expression);
        } catch (ScriptException e) {
            LOGGER.error("Expression [" + expression + "] cannot be evaluated.");
            result = null;
        }
        return result;
    }

    private static class SingletonHolder {
        protected static final Evaluator _instance = new Evaluator();
    }
}
