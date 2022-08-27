package me.syberiak.cmdr.ui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MMMenuBar extends JMenuBar {

    public MMMenuBar() {
        super();

        JMenu menu = new JMenu("...");
        JMenuItem settingsItem = new JMenuItem("Settings");
        JMenuItem aboutItem = new JMenuItem("About");

        settingsItem.addActionListener(e -> new MMSettingsDialog());
        aboutItem.addActionListener(e -> new MMAboutDialog());

        menu.add(settingsItem); menu.add(aboutItem);
        this.add(menu);
    }
}
