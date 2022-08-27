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
import me.syberiak.cmdr.util.Pair;
import me.syberiak.cmdr.util.OGGAudioConverter;

public class ManagerMenu extends JFrame {

    JLabel discLabel;
    JLabel statusBar;

    ImageIcon defaultDiscIcon;

    public ManagerMenu() {
        this.setTitle("CMDR Manager");
        this.setLayout(new BorderLayout());
        if (CMDR.ICON_URL != null) {
            defaultDiscIcon = new ImageIcon(CMDR.ICON_URL);
        }

        MMMenuBar menuBar = new MMMenuBar();

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2));
        buttonsPanel.setPreferredSize(new Dimension(400, 100));
        try {
            List<String[]> musicDiscsData = CMDR.MUSIC_DATA;
            for (String[] item : musicDiscsData) {
                String author = item[0];
                String song = item[1];

                String fullName = String.format("%s - %s", author, song);
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

        JLabel modName = new JLabel("CMDR v" + CMDR.VERSION);
        modName.setFont(new Font(null, Font.BOLD, 20));
        JTextArea modDescription = new JTextArea("This is a placeholder of description where I need to tell you how to use program. Or maybe there will be some patch notes. Dunno.");
        modDescription.setWrapStyleWord(true);
        modDescription.setLineWrap(true);
        modDescription.setOpaque(false);
        modDescription.setEditable(false);
        modDescription.setFocusable(false);
        modDescription.setBackground(UIManager.getColor("Label.background"));
        modDescription.setFont(UIManager.getFont("Label.font"));
        modDescription.setBorder(UIManager.getBorder("Label.border"));
        
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
        labelsPanel.add(modName, c);

        c.insets = new Insets(10, 2, 0, 2);
        c.anchor = GridBagConstraints.CENTER;
        labelsPanel.add(modDescription, c);
        
        c.insets = new Insets(25, 0, 0, 10);
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
        this.setBounds(150, 150, 640, 500);
    }
    
    void changeDiscLabel(ImageIcon icon, String discFullName) {
        discLabel.setIcon(icon);
        discLabel.setText(discFullName);
    }

    void setDefaultRecord(String record) {
        File recordFile = new File(CMDR.RECORD_DIR + record);
        if (recordFile.exists()) {
            if (recordFile.delete()) {
                System.out.println(record + ": returned default record successfully.");
            }
            else {
                System.out.println(record + ": failed to return default record.");
            }
        }
    }

    void setCustomRecord(String record) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("MP3/WAV audio (*.mp3, *.wav)", "mp3", "wav"));

        int fcOption = fc.showOpenDialog(this);
        if (fcOption == JFileChooser.APPROVE_OPTION) {
            statusBar.setText("Trying to convert your audio...");
            try {
                Pair<Boolean, Exception> result = OGGAudioConverter.convert(fc.getSelectedFile().getAbsolutePath(),
                        CMDR.RECORD_DIR + record);
                boolean success = result.element0();
                if (success) {
                    statusBar.setText("Done!");
                } else {
                    Exception exception = result.element1();

                    JOptionPane.showMessageDialog(this,
                            "Got an exception while converting: " + exception, "CMDR Manager",
                            JOptionPane.WARNING_MESSAGE);
                    statusBar.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
                statusBar.setText("");
            }
        }
    }
}
