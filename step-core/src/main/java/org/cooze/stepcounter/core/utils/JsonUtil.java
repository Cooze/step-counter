package org.cooze.stepcounter.core.utils;

import com.google.gson.Gson;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public final class JsonUtil {
    private final static Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> String toJson(T obj) {
        return gson.toJson(obj);
    }
}
