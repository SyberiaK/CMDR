package me.syberiak.cmdr.ui.settings;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.config.Config;
import me.syberiak.cmdr.rp.Launcher;
import me.syberiak.cmdr.rp.ResourcePack;
import me.syberiak.cmdr.ui.component.CheckList;

import javax.swing.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class PrismSettingsForm extends SettingsForm {
    private JPanel container;
    private JTextField setPathToPrismTextField;
    private JButton setPathToPrismChooseDirectory;
    private CheckList checkList1;

    public PrismSettingsForm() {
        super(Launcher.Prism);

        setPathToPrismTextField.setText(Config.getInstance().getPrismPath());
        setPathToPrismTextField.setToolTipText(String.format("Path to Prism Launcher directory (default: %s)",
                CMDR.DEFAULT_PRISM_PATH));

        setPathToPrismChooseDirectory.addActionListener(e -> choosePrismDirectory());
    }

    public JPanel getContainer() {
        return container;
    }

    private void choosePrismDirectory() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(new File(CMDR.CURRENT_USER_ROAMING));

        if (fc.showOpenDialog(this.container) == JFileChooser.APPROVE_OPTION) {
            setPathToPrismTextField.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    public void onSave() {
        try {
            Config config = Config.getInstance();
            String path = setPathToPrismTextField.getText();
            if (CMDR.isPrismDirectory(path)) {
                config.setPrismPath(path);
            } else {
                int wrongDirectory = JOptionPane.showOptionDialog(this.container,
                        "The directory you chose doesn't seem to be a Prism Launcher directory. Want to continue?",
                        "CMDR Manager",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        null,
                        null);
                if (wrongDirectory == JOptionPane.YES_OPTION) {
                    config.setPrismPath(path);
                } else {
                    setPathToPrismTextField.setText(config.getPrismPath());
                }
            }
            System.out.println(Arrays.toString(checkList1.getSelectedItemsLabels()));
            config.setPrismInstances(checkList1.getSelectedItemsLabels());
            CMDR.syncWithConfig();
            Arrays.stream(config.getPrismInstances())
                    .forEach(inst -> ResourcePack.initialize(config.getPrismPath() + "/instances/" + inst));
        } catch (Exception e) {
            CMDR.throwError(e);
        }
    }

    public boolean anyChanges() {
        Config config = Config.getInstance();

        String normalizedPath = Paths.get(setPathToPrismTextField.getText()).toString();

        boolean arePathsEquals = config.getPrismPath().equals(normalizedPath);
        boolean areSelectedInstancesEquals = Arrays.equals(config.getPrismInstances(),
                checkList1.getSelectedItemsLabels());

        return !(arePathsEquals && areSelectedInstancesEquals);
    }

    private void createUIComponents() {
        String[] prismInstances = CMDR.parsePrismInstances();
        checkList1 = new CheckList(prismInstances);
        Arrays.stream(checkList1.getItems())
                .filter(item -> Arrays.asList(Config.getInstance().getPrismInstances()).contains(item.toString()))
                .forEach(item -> item.setSelected(true));
    }
}