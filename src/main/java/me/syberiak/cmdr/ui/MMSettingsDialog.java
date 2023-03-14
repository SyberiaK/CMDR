package me.syberiak.cmdr.ui;

import javax.swing.*;
import java.awt.event.*;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.config.Config;
import me.syberiak.cmdr.rp.Launcher;
import me.syberiak.cmdr.ui.settings.PrismSettingsForm;
import me.syberiak.cmdr.ui.settings.SettingsForm;
import me.syberiak.cmdr.ui.settings.VanillaSettingsForm;

public class MMSettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> selectLauncherComboBox;
    private JPanel formContainer;
    private SettingsForm settingsForm;

    public MMSettingsDialog() {
        selectLauncherComboBox.setSelectedItem(Config.getInstance().getSelectedLauncher().name);
        onChangeLauncher();

        setModal(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        pack();
        setMinimumSize(getSize());
        setLocation(ManagerMenu.WINDOW_POSITION);
        setVisible(true);

        selectLauncherComboBox.addActionListener(e -> onChangeLauncher());

        // bottom buttons panel
        buttonOK.addActionListener(e -> onSave());

        buttonCancel.addActionListener(e -> onClose());

        // call onClose() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        // call onClose() on ESCAPE
        contentPane.registerKeyboardAction(e -> onClose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onChangeLauncher() {
        if (selectLauncherComboBox.getSelectedItem() == null) {
            return;
        }
        String selectedLauncher = selectLauncherComboBox.getSelectedItem().toString();

        CMDR.LOGGER.info("Changed launcher to {}", selectLauncherComboBox.getSelectedItem().toString());
        switch (Launcher.valueOf(selectedLauncher)) {
            case Vanilla:
                settingsForm = new VanillaSettingsForm();
                break;
            case Prism:
                settingsForm = new PrismSettingsForm();
                break;
            default:
                break;
        }
        formContainer.removeAll();
        formContainer.add(settingsForm.getContainer());
        formContainer.revalidate();
        formContainer.repaint();
        pack();
    }

    private void onSave() {
        Config.getInstance().selectLauncher(settingsForm.launcher);
        settingsForm.onSave();
        Config.getInstance().toFile(CMDR.SETTINGS_DIR);
    }

    private void onClose() {
        try {
            if (!settingsForm.anyChanges()) {
                dispose();
                return;
            }

            int option = JOptionPane.showOptionDialog(this,
                    "Are you sure you want to close? You will lose all unsaved changes.", "CMDR Manager",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    null,
                    null);
            if (option == JOptionPane.YES_OPTION) {
                dispose();
            }
        } catch (Exception e) {
            CMDR.throwError(e);
        }
    }

    private void createUIComponents() {
        ImageIcon icon = CMDR.manager.defaultDiscIcon;
        if (icon != null) {
            setIconImage(icon.getImage());
        }
        setTitle("CMDR Manager");

        selectLauncherComboBox = new JComboBox<>(CMDR.SUPPORTED_LAUNCHERS);
        selectLauncherComboBox.setFocusable(false);
    }
}
