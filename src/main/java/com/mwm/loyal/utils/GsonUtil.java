package com.mwm.loyal.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class GsonUtil {
    public static <T> T getBeanFromJson(String json, Class<T> tClass) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, tClass);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBeanFromJson(HttpServletRequest request, String param, Class<T> tClass) {
        String json = request.getParameter(param);
        System.out.println(json + " : " + tClass.getName());
        return getBeanFromJson(json, tClass);
    }

    public static <T> List<T> getBeanListFromJson(String json, Class<T> tClass) {
        List<T> list = new ArrayList<>();
        try {
            if (json.isEmpty()) {
                list.clear();
                return list;
            }
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add((T) new Gson().fromJson(elem, tClass));
            }
        } catch (Exception e) {
            //
        }
        return list;
    }
}