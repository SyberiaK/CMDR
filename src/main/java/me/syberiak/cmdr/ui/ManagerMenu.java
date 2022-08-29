package me.syberiak.cmdr.ui;

import java.io.File;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.net.URL;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.util.OGGAudioConverter;

public class ManagerMenu extends JFrame {

    static final int FRAME_WIDTH = 640;
    static final int FRAME_HEIGHT = 500;
    JLabel discLabel;
    JLabel statusBar;

    ImageIcon defaultDiscIcon;

    public ManagerMenu() {
        this.setTitle("CMDR Manager");
        this.setLayout(new BorderLayout());
        if (CMDR.ICON_URL != null) {
            defaultDiscIcon = new ImageIcon(CMDR.ICON_URL);
        }

        if (UIManager.getLookAndFeel().isSupportedLookAndFeel()) {
            final String platform = UIManager.getSystemLookAndFeelClassName();
            if (!UIManager.getLookAndFeel().getName().equals(platform)) {
                try {
                    UIManager.setLookAndFeel(platform);
                } catch (Exception e) {
                    CMDR.LOGGER.error("Exception occurred!", e);
                }
            }
        }

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

        JLabel appName = new JLabel("CMDR v" + CMDR.VERSION);
        appName.setFont(new Font(null, Font.BOLD, 20));
        JTextArea appDescription = new JTextArea(String.join("\n", CMDR.CHANGELOG));
        appDescription.setWrapStyleWord(true);
        appDescription.setLineWrap(true);
        appDescription.setOpaque(false);
        appDescription.setEditable(false);
        appDescription.setFocusable(false);
        appDescription.setBackground(UIManager.getColor("Label.background"));
        appDescription.setFont(UIManager.getFont("Label.font"));
        appDescription.setBorder(UIManager.getBorder("Label.border"));
        
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
        labelsPanel.add(appDescription, c);
        
        c.insets = new Insets(20, 0, 0, 10);
        labelsPanel.add(discLabel, c);

        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(100, 2, 5, 0);
        labelsPanel.add(statusBar, c);

        this.setJMenuBar(menuBar);
        this.add(buttonsPanel, BorderLayout.LINE_START);
        this.add(labelsPanel, BorderLayout.LINE_END);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(defaultDiscIcon.getImage());
        this.setResizable(false);
        this.setBounds(150, 150, FRAME_WIDTH, FRAME_HEIGHT);
    }
    
    void changeDiscLabel(ImageIcon icon, String discFullName) {
        discLabel.setIcon(icon);
        discLabel.setText(discFullName);
    }

    void setDefaultRecord(String record) {
        File recordFile = new File(CMDR.RECORD_DIR + record);
        if (recordFile.exists()) {
            if (recordFile.delete()) {
                CMDR.LOGGER.info(record + ": returned default record successfully.");
            }
            else {
                CMDR.LOGGER.error(record + ": failed to return default record.");
            }
        }
    }

    void setCustomRecord(String record) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("MP3/WAV/OGG audio (*.mp3, *.wav, *.ogg)",
                "mp3", "wav", "ogg"));

        int fcOption = fc.showOpenDialog(this);
        if (fcOption == JFileChooser.APPROVE_OPTION) {
            statusBar.setText("Trying to convert your audio...");
            try {
                String source = fc.getSelectedFile().getAbsolutePath();
                String target = CMDR.RECORD_DIR + record;

                OGGAudioConverter.convert(source, target);
                CMDR.LOGGER.info(source + ": converted successfully to " + target);
                statusBar.setText("Done!");
            } catch (Exception e) {
                CMDR.LOGGER.error("Converting exception occurred!", e);
                JOptionPane.showMessageDialog(this,
                        "Got an exception while converting:\n" + e, "CMDR Manager",
                        JOptionPane.WARNING_MESSAGE);
                statusBar.setText("");
            }
        }
    }
}
