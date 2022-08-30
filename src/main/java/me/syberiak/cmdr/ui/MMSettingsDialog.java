package me.syberiak.cmdr.ui;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.settings.Settings;
import me.syberiak.cmdr.settings.SettingsContainer;

public class MMSettingsDialog extends JDialog  {

    static final int DIALOG_WIDTH = 480;
    static final int DIALOG_HEIGHT = 360;

    public MMSettingsDialog() {
        super(CMDR.manager, "CMDR Manager", true);

        if (CMDR.ICON_URL != null) {
            ImageIcon discImageIcon = new ImageIcon(CMDR.ICON_URL);
            this.setIconImage(discImageIcon.getImage());
        }

        this.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();

        JPanel pathToMinecraftInput = new JPanel(new GridBagLayout());

        JLabel pathToMinecraftLabel = new JLabel("Minecraft directory:");
        JTextField pathToMinecraftTextField = new JTextField();
        pathToMinecraftTextField.setToolTipText(String.format("Path to Minecraft directory (default: %s)",
                CMDR.DEFAULT_MINECRAFT_PATH));
        pathToMinecraftTextField.setText(CMDR.MINECRAFT_PATH);
        pathToMinecraftTextField.setPreferredSize(new Dimension(300, 25));

        pathToMinecraftInput.setAlignmentX(0);
        pathToMinecraftInput.setAlignmentY(0);
        JButton pathToMinecraftChooseFile = new JButton("...");
        pathToMinecraftChooseFile.setToolTipText("Choose the Minecraft directory");
        pathToMinecraftChooseFile.setFocusable(false);
        pathToMinecraftChooseFile.setPreferredSize(new Dimension(25, 25));

        pathToMinecraftChooseFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(CMDR.CURRENT_USER_ROAMING));

            int fcOption = fc.showOpenDialog(this);
            if (fcOption == JFileChooser.APPROVE_OPTION) {
                pathToMinecraftTextField.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        JPanel buttonsPanel = new JPanel(new GridBagLayout());

        JPanel buttonsMargin = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(50, 25));
        saveButton.setFocusable(false);

        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(50, 25));
        closeButton.setFocusable(false);

        saveButton.addActionListener(o -> {
            try {
                SettingsContainer settings = Settings.readSettings(CMDR.SETTINGS_DIR);
                Path path = Paths.get(pathToMinecraftTextField.getText());
                if (CMDR.isMinecraftDirectory(path)) {
                    settings.setPathToMinecraft(path.toString());
                } else {
                    int wrongDirectory = JOptionPane.showOptionDialog(this, "The directory you chose doesn't seem " +
                                    "to be a Minecraft directory. Want to continue?", "CMDR Manager",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, null,null);
                    if (wrongDirectory == JOptionPane.YES_OPTION) {
                        settings.setPathToMinecraft(path.toString());
                    } else {
                        pathToMinecraftTextField.setText(CMDR.MINECRAFT_PATH);
                    }
                }
                Settings.editSettings(CMDR.SETTINGS_DIR, settings);
                CMDR.getSettings();
            } catch (Exception e) { CMDR.throwError(e); }
        });

        closeButton.addActionListener(e -> this.dispatchEvent(
                new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        c.insets = new Insets(0, 0, 0, 10);
        c.gridx = 0;
        pathToMinecraftInput.add(pathToMinecraftLabel, c);

        c.gridy = 1;
        c.gridwidth = 2;
        pathToMinecraftInput.add(pathToMinecraftTextField, c);
        c.gridx = 2;
        pathToMinecraftInput.add(pathToMinecraftChooseFile, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = GridBagConstraints.RELATIVE;
        c.ipadx = 200;
        c.gridwidth = 2;
        buttonsPanel.add(buttonsMargin, c);

        c.ipadx = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(0, 0, 10, 10);
        c.gridwidth = 1;
        buttonsPanel.add(saveButton, c);
        buttonsPanel.add(closeButton, c);

        this.add(pathToMinecraftInput, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.PAGE_END);
        this.setBounds(150, 150, DIALOG_WIDTH, DIALOG_HEIGHT);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    Window window = we.getWindow();
                    SettingsContainer settings = Settings.readSettings(CMDR.SETTINGS_DIR);
                    if (!settings.getPathToMinecraft().equals(pathToMinecraftTextField.getText())) {
                        int option = JOptionPane.showOptionDialog(window,
                                "Are you sure you want to close? " +
                                        "You will lose all unsaved changes.", "CMDR Manager",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, null,null);
                        if (option == JOptionPane.YES_OPTION) {
                            window.dispose();
                        }
                    } else {
                        window.dispose();
                    }
                } catch (Exception e) { CMDR.throwError(e); }
            }
        });
        this.setResizable(false);
        this.setVisible(true);
    }
}
