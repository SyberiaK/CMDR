package me.syberiak.cmdr.settings;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Settings {

    public static SettingsContainer readSettings(String source) throws IOException {
        return new ObjectMapper().readValue(new File(source), SettingsContainer.class);
    }

    public static void editSettings(String source, SettingsContainer newSettings) throws IOException {
        new ObjectMapper().writeValue(new File(source), newSettings);
    }
}
