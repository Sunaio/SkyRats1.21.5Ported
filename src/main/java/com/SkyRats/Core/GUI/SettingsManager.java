package com.SkyRats.Core.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class SettingsManager {

    private static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    // Save file type
    public static <T> void save(Map<String, List<T>> settings, File file) {
        FileWriter writer = null;
        try {
            file.getParentFile().mkdirs();
            writer = new FileWriter(file);
            GSON.toJson(settings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    // Load file type
    public static <T> Map<String, List<T>> load(File file, final Class<T> clazz) {
        if (!file.exists()) return new HashMap<>();

        FileReader reader = null;
        try {
            reader = new FileReader(file);

            // Use raw Map type for initial deserialization
            Type mapType = new TypeToken<Map<String, List>>(){}.getType();

            // Deserialize raw map
            Map<String, List> rawMap = GSON.fromJson(reader, mapType);
            if (rawMap == null) return new HashMap<>();

            Map<String, List<T>> result = new HashMap<>();
            for (Map.Entry<String, List> entry : rawMap.entrySet()) {
                List<T> typedList = new ArrayList<>();
                for (Object o : entry.getValue()) {
                    String json = GSON.toJson(o);
                    T obj = GSON.fromJson(json, clazz);
                    typedList.add(obj);
                }
                result.put(entry.getKey(), typedList);
            }

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        return new HashMap<>();
    }
}
