package com.mwm.loyal.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javax.servlet.http.HttpServletRequest;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GsonUtil {
    private static Gson gson = new Gson();

    public static <T> T json2Bean(String json, Class<T> tClass) {
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        return gson.fromJson(reader, tClass);
    }

    public static <T> T json2Bean(HttpServletRequest request, String param, Class<T> tClass) {
        String json = request.getParameter(param);
        return json2Bean(json, tClass);
    }

    public static <T> String bean2Json(Object tClass) {
        if (null == tClass)
            return "{}";
        return gson.toJson(tClass);
    }

    public static <T> List<T> json2List(String json, Class<T> tClass) {
        List<T> list = new ArrayList<>();
        try {
            if (json.isEmpty()) {
                list.clear();
                return list;
            }
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, tClass));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }
}