package me.syberiak.cmdr.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsContainer {

    @JsonProperty("pathToMinecraft")
    public String pathToMinecraft;

    @JsonCreator
    public SettingsContainer(@JsonProperty("pathToMinecraft") String pathToMinecraft) {
        this.pathToMinecraft = pathToMinecraft;
    }

    public String getPathToMinecraft() { return pathToMinecraft; }
    public void setPathToMinecraft(String pathToMinecraft) { this.pathToMinecraft = pathToMinecraft; }
}
