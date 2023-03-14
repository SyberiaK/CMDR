package me.syberiak.cmdr.ui.component;

import me.syberiak.cmdr.ui.ReworkedMMAboutDialog;
import me.syberiak.cmdr.ui.MMSettingsDialog;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MMMenuBar extends JMenuBar {

    public MMMenuBar() {
        JMenu menu = new JMenu("...");
        JMenuItem settingsItem = new JMenuItem("Settings");
        JMenuItem aboutItem = new JMenuItem("About");

        settingsItem.addActionListener(e -> new MMSettingsDialog());
        aboutItem.addActionListener(e -> new ReworkedMMAboutDialog());

        menu.add(settingsItem);
        menu.add(aboutItem);

        add(menu);
    }
}
