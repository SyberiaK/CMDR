package me.syberiak.cmdr.ui;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.*;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.config.Config;
import me.syberiak.cmdr.io.FileManager;
import me.syberiak.cmdr.ui.component.MMDiscButton;
import me.syberiak.cmdr.ui.component.MMMenuBar;

public class ManagerMenu extends JFrame {

    static final Dimension WINDOW_SIZE = new Dimension(640, 500);
    static final Point WINDOW_POSITION = new Point(150, 150);

    JLabel discLabel;
    JLabel statusBar;

    public ImageIcon defaultDiscIcon;

    public ManagerMenu() {
        setTitle("CMDR Manager");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(WINDOW_POSITION);
        setSize(WINDOW_SIZE);
        setResizable(false);

        if (CMDR.ICON_URL != null) {
            defaultDiscIcon = new ImageIcon(CMDR.ICON_URL);
            setIconImage(defaultDiscIcon.getImage());
        }

        if (UIManager.getLookAndFeel().isSupportedLookAndFeel()) {
            final String platform = UIManager.getSystemLookAndFeelClassName();
            if (!UIManager.getLookAndFeel().getName().equals(platform)) {
                try {
                    UIManager.setLookAndFeel(platform);
                } catch (Exception e) {
                    CMDR.LOGGER.warn("Exception occurred!", e);
                }
            }
        }

        createUIComponents();
    }

    void createUIComponents() {
        MMMenuBar menuBar = new MMMenuBar();

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2));
        buttonsPanel.setPreferredSize(new Dimension(400, 100));
        try {
            List<String[]> musicDiscsData = CMDR.MUSIC_DATA;
            for (String[] item : musicDiscsData) {
                String author = item[0];
                String song = item[1];

                String fullName = author + " - " + song;
                String audio = song.toLowerCase() + ".ogg";
                String icon = song.toLowerCase() + ".png";
                URL iconURL = CMDR.class.getResource("/sprites/" + icon);

                MMDiscButton b = new MMDiscButton(song, fullName, audio, iconURL);
                buttonsPanel.add(b);
            }
        } catch (Exception e) { throw new RuntimeException(e); }

        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        labelsPanel.setPreferredSize(new Dimension(200, 100));

        JLabel appName = new JLabel("CMDR " + CMDR.VERSION_V);
        appName.setFont(new Font(null, Font.BOLD, 20));
        JTextArea appChangelog = new JTextArea(String.join("\n", CMDR.LATEST_CHANGELOG));
        appChangelog.setWrapStyleWord(true);
        appChangelog.setLineWrap(true);
        appChangelog.setEditable(false);
        appChangelog.setFocusable(false);
        appChangelog.setBackground(UIManager.getColor("Label.background"));
        appChangelog.setFont(UIManager.getFont("Label.font"));
        appChangelog.setBorder(UIManager.getBorder("Label.border"));

        discLabel = new JLabel("Choose music disc...");
        discLabel.setIcon(defaultDiscIcon);
        discLabel.setHorizontalTextPosition(JLabel.CENTER);
        discLabel.setVerticalTextPosition(JLabel.BOTTOM);

        statusBar = new JLabel(" ");

        c.ipadx = 5;
        c.ipady = 5;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.insets = new Insets(10, 2, 0, 0);
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        labelsPanel.add(appName, c);

        c.insets = new Insets(10, 2, 5, 2);
        c.anchor = GridBagConstraints.CENTER;
        labelsPanel.add(appChangelog, c);

        c.insets = new Insets(20, 0, 0, 10);
        labelsPanel.add(discLabel, c);

        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(75, 2, 5, 0);
        labelsPanel.add(statusBar, c);

        setJMenuBar(menuBar);
        add(buttonsPanel, BorderLayout.LINE_START);
        add(labelsPanel, BorderLayout.LINE_END);
    }
    
    public void changeDiscLabel(ImageIcon icon, String discFullName) {
        discLabel.setIcon(icon);
        discLabel.setText(discFullName);
    }

    boolean revertRecord(String recordName) {
        try {
            switch (Config.getInstance().getSelectedLauncher()) {
                case Vanilla:
                    FileManager.revertRecordForVanilla(recordName);
                    return true;
                case Prism:
                    FileManager.revertRecordForPrism(recordName);
                    return true;
                default:
                    throw new Exception("Found unknown launcher");
            }
        } catch (Exception e) {
            CMDR.LOGGER.error("Converting exception occurred!", e);
            JOptionPane.showMessageDialog(this,
                    "Converting exception occurred!\n" + e, "CMDR Manager",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    boolean setRecord(File source, String recordName) {
        statusBar.setText("Trying to convert your audio...");

        try {
            switch (Config.getInstance().getSelectedLauncher()) {
                case Vanilla:
                    FileManager.setCustomRecordForVanilla(source, recordName);
                    break;
                case Prism:
                    FileManager.setCustomRecordForPrism(source, recordName);
                    break;
                default:
                    throw new Exception("Found unknown launcher");
            }

            statusBar.setText("Done!");
            return true;
        } catch (Exception e) {
            CMDR.LOGGER.error("Converting exception occurred!", e);
            JOptionPane.showMessageDialog(this,
                    "Converting exception occurred!\n" + e, "CMDR Manager",
                    JOptionPane.WARNING_MESSAGE);
            statusBar.setText("");
            return false;
        }
    }
}
