package me.syberiak.cmdr.ui.settings;


import me.syberiak.cmdr.config.Launcher;

import javax.swing.*;

public abstract class SettingsForm {
    public final Launcher launcher;

    SettingsForm(Launcher launcher) {
        this.launcher = launcher;
    }

    // Need to implement in each individual class
    // because of IDEA UI Designer
    public abstract JPanel getContainer();
    public abstract void onSave();
    public abstract boolean anyChanges();
}

