package me.syberiak.cmdr.ui.settings;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.config.Config;
import me.syberiak.cmdr.rp.Launcher;
import me.syberiak.cmdr.rp.ResourcePack;

import javax.swing.*;
import java.io.File;
import java.nio.file.Paths;

public class VanillaSettingsForm extends SettingsForm {
    private JPanel container;
    private JTextField setPathToMinecraftTextField;
    private JButton setPathToMinecraftChooseDirectory;

    public VanillaSettingsForm() {
        super(Launcher.Vanilla);

        setPathToMinecraftTextField.setText(Config.getInstance().getVanillaPath());
        setPathToMinecraftTextField.setToolTipText(String.format("Path to Minecraft directory (default: %s)",
                CMDR.DEFAULT_MINECRAFT_PATH));

        setPathToMinecraftChooseDirectory.addActionListener(e -> chooseMinecraftDirectory());
    }

    public JPanel getContainer() {
        return container;
    }

    private void chooseMinecraftDirectory() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(new File(CMDR.CURRENT_USER_ROAMING));

        if (fc.showOpenDialog(this.container) == JFileChooser.APPROVE_OPTION) {
            setPathToMinecraftTextField.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    public void onSave() {
        try {
            Config config = Config.getInstance();
            String path = setPathToMinecraftTextField.getText();
            if (CMDR.isMinecraftDirectory(path)) {
                config.setVanillaPath(path);
            } else {
                int wrongDirectory = JOptionPane.showOptionDialog(this.container,
                        "The directory you chose doesn't seem to be a Minecraft directory. Want to continue?",
                        "CMDR Manager",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        null,
                        null);
                if (wrongDirectory == JOptionPane.YES_OPTION) {
                    config.setVanillaPath(path);
                } else {
                    setPathToMinecraftTextField.setText(config.getVanillaPath());
                }
            }
            CMDR.syncWithConfig();
            ResourcePack.initialize(config.getVanillaPath());
        } catch (Exception e) {
            CMDR.throwError(e);
        }
    }

    public boolean anyChanges() {
        Config config = Config.getInstance();
        String normalizedPath = Paths.get(setPathToMinecraftTextField.getText()).toString();

        return !(config.getVanillaPath().equals(normalizedPath));
    }
}