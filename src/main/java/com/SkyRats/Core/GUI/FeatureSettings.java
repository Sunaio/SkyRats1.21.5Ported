package com.SkyRats.Core.GUI;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class FeatureSettings {

    private static final File SETTINGS_PATH = Paths.get("SkyRats", "settings.json").toFile();

    private static final Map<String, List<Settings>> featureSettings = new HashMap<>();

    public static void run() {
        // Load saved settings, merging them with defaults
        var loaded = SettingsManager.load(SETTINGS_PATH, Settings.class);
        if (loaded != null) {
            featureSettings.putAll(loaded);
        }

        registerDefaults();

        SettingsManager.save(featureSettings, SETTINGS_PATH);
    }

    private static void registerDefaults() {
        register("Mining", List.of(
                setting("Mineshaft Tracker", "Tracks all mineshaft types that you have entered", false)
        ));

        register("Alerts", List.of(
                setting("Mining Ability Cooldown Notification", "Alerts you when your ability is off cooldown", false),
                setting("Reminder to Switch Ability Notification", "Alerts you to switch back to Pickobulus after leaving minable mineshaft", false)
        ));
    }

    private static void register(String featureName, List<Settings> defaults) {
        var existing = featureSettings.get(featureName);

        if (existing == null) {
            featureSettings.put(featureName, new ArrayList<>(defaults));
        } else {
            for (var def : defaults) {
                boolean found = false;
                for (var e : existing) {
                    if (e.getLabel().equalsIgnoreCase(def.getLabel())) {
                        if (e.getDescription() == null || e.getDescription().isEmpty()) {
                            e.setDescription(def.getDescription());
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    existing.add(def);
                }
            }
        }
    }

    private static Settings setting(String label, String description, boolean defaultValue) {
        return new Settings(label, description, defaultValue);
    }

    public static Map<String, List<Settings>> getFeatureSettings() {
        return featureSettings;
    }

    public static boolean isFeatureEnabled(String feature, String label) {
        var settingsList = featureSettings.get(feature);
        if (settingsList != null) {
            for (var s : settingsList) {
                if (s.getLabel().equalsIgnoreCase(label)) {
                    return s.getValue();
                }
            }
        }
        return false;
    }
}
