package me.syberiak.cmdr.ui;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.config.Config;
import me.syberiak.cmdr.io.ResourcePack;
import me.syberiak.cmdr.util.OGGAudioConverter;

public class MMDiscDialog extends JDialog {

    static final int DIALOG_WIDTH = 480;
    static final int DIALOG_HEIGHT = 360;

    public MMDiscDialog(String discFullName, String recordName, ImageIcon discIcon) {
        super(CMDR.manager, "CMDR Manager", true);
        this.setLayout(new BorderLayout());
        Config config = Config.getInstance();
        Path recordPath;
        switch (config.getSelectedLauncher()) {
            case Vanilla:
                recordPath = Paths.get(config.getVanillaPath() + ResourcePack.recordsPlacement + recordName);
                break;
            case Prism:
                recordPath = Paths.get(""); // todo: write an algorithm to check RP content in instances
                break;
            default:
                Exception e = new Exception("Found unknown launcher");
                CMDR.LOGGER.error("Exception occurred!", e);
                JOptionPane.showMessageDialog(this,
                        "Exception occurred!\n" + e, "CMDR Manager",
                        JOptionPane.WARNING_MESSAGE);
                return;
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        
        JLabel l = new JLabel(String.format("Choose what you want to do with \"%s\":", discFullName));
        l.setIcon(new ImageIcon(discIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        l.setPreferredSize(new Dimension(50, 75));
        l.setVerticalAlignment(JLabel.BOTTOM); 
        l.setHorizontalAlignment(JLabel.CENTER);

        JPanel contentPanel = new JPanel(new GridBagLayout());

        JButton setDefaultButton = new JButton("Set default record");
        setDefaultButton.setFocusable(false);
        if (!recordPath.toFile().exists()) { setDefaultButton.setEnabled(false); }
        JButton setCustomButton = new JButton("Set custom record");
        setCustomButton.setFocusable(false);

        JPanel bottomMargin = new JPanel();
        bottomMargin.setPreferredSize(new Dimension(50, 60));

        setDefaultButton.addActionListener(e -> {
            boolean result = CMDR.manager.revertRecord(recordName);
            if (result) {
                setDefaultButton.setText("Done!");
                setDefaultButton.setEnabled(false);
            }
        });

        setCustomButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("MP3/WAV/OGG audio (*.mp3, *.wav, *.ogg)",
                    OGGAudioConverter.SUPPORTED_FORMATS));

            int fcOption = fc.showOpenDialog(this);
            if (fcOption == JFileChooser.APPROVE_OPTION) {
                boolean result = CMDR.manager.setRecord(fc.getSelectedFile(), recordName);
                if (result) {
                    setDefaultButton.setText("Set default record");
                }
            }
            if (recordPath.toFile().exists()) { setDefaultButton.setEnabled(true); }
        });

        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        contentPanel.add(setDefaultButton, c);
        contentPanel.add(setCustomButton, c);

        this.add(l, BorderLayout.PAGE_START);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(bottomMargin, BorderLayout.PAGE_END);

        this.setLocation(ManagerMenu.WINDOW_POSITION);
        this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        this.setIconImage(discIcon.getImage());
        this.setResizable(false);
        this.setVisible(true);
    }
}
