package com.wonders.xlab.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by wangqiang on 15/9/6.
 */
public final class PropertyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);

    private static final Properties PROPS = new Properties();

    static {
        InputStream stream = PropertyUtils.class.getResourceAsStream("/config/application.properties");
        try {
            PROPS.load(stream);
            stream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private PropertyUtils() {

    }

    public static String getProperty(String key) {
        return PROPS.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }

}
