package com.zgj.mps.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.MissingResourceException;

/**
 * Created by user on 2019/12/9.
 */
@Component
public class PropertiesUtil {
    private static Environment env;

    @Autowired
    protected void set(Environment env) throws IOException {
        PropertiesUtil.env = env;
    }

    public static String getString(String key) {
        try {
            return env.getProperty(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public static String getString(String key, String defaultValue) {
        try {
            String value = env.getProperty(key);
            if (value == null) {
                return defaultValue;
            }
            return value;
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }
}
