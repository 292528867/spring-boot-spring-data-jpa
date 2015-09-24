package com.wonders.xlab.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by wangqiang on 15/9/24.
 */
public final class ObjectMapperUtils {

    private static ObjectMapper objectMapper;

    private ObjectMapperUtils() {

    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

}
