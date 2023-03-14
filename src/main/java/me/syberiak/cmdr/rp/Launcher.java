package me.syberiak.cmdr.rp;

import java.util.Arrays;

public enum Launcher {
    None("None"),
    Vanilla("Vanilla"),
    Prism("Prism");

    public final String id;
    public final String name;

    Launcher(String name) {
        this.id = name.toLowerCase();
        this.name = name;
    }

    Launcher(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Launcher findLauncherByID(String id) {
        return Arrays.stream(Launcher.values()).filter(v ->
                v.id.equals(id)).findFirst().orElse(Vanilla);
    }
}
