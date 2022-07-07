package me.syberiak.cmdr.manager;

import java.io.File;
/* import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption; */
import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import me.syberiak.cmdr.util.AudioConverter;

public class ManagerMenu extends JFrame {

    static ManagerMenu managerMenu;
    static JLabel discLabel;
    public static JLabel statusBar;

    static ImageIcon defaultDiscIcon;

    static String RECORD_DIR;
    static String ICON_DIR;
    static String SPRITES_DIR;
    //private static String DEFAULT_RECORDS_DIR;
    private static String MOD_VERSION;

    ManagerMenu() {
        super("CMDR Manager");
        this.setLayout(new BorderLayout());
        defaultDiscIcon = new ImageIcon(ICON_DIR);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2));
        buttonsPanel.setPreferredSize(new Dimension(400, 100));

        MMDiscButton button13 = new MMDiscButton(MMDiscButton.MENU, "13", "C418 - 13", "13.ogg", "13.png");
        MMDiscButton buttonCat = new MMDiscButton(MMDiscButton.MENU, "сat", "C418 - cat", "cat.ogg", "cat.png");
        MMDiscButton buttonBlocks = new MMDiscButton(MMDiscButton.MENU, "blocks", "C418 - blocks", "blocks.ogg", "blocks.png");
        MMDiscButton buttonChirp = new MMDiscButton(MMDiscButton.MENU, "chirp", "C418 - chirp", "chirp.ogg", "chirp.png");
        MMDiscButton buttonFar = new MMDiscButton(MMDiscButton.MENU, "far", "C418 - far", "far.ogg", "far.png");
        MMDiscButton buttonMall = new MMDiscButton(MMDiscButton.MENU, "mall", "C418 - mall", "mall.ogg", "mall.png");
        MMDiscButton buttonMellohi = new MMDiscButton(MMDiscButton.MENU, "mellohi", "C418 - mellohi", "mellohi.ogg", "mellohi.png");
        MMDiscButton buttonStal = new MMDiscButton(MMDiscButton.MENU, "stal", "C418 - stal", "stal.ogg", "stal.png");
        MMDiscButton buttonStrad = new MMDiscButton(MMDiscButton.MENU, "strad", "C418 - strad", "strad.ogg", "strad.png");
        MMDiscButton buttonWard = new MMDiscButton(MMDiscButton.MENU, "ward", "C418 - ward", "ward.ogg", "ward.png");
        MMDiscButton button11 = new MMDiscButton(MMDiscButton.MENU, "11", "C418 - 11", "11.ogg", "11.png");
        MMDiscButton buttonWait = new MMDiscButton(MMDiscButton.MENU, "wait", "C418 - wait", "wait.ogg", "wait.png");
        MMDiscButton buttonOtherside = new MMDiscButton(MMDiscButton.MENU, "otherside", "Lena Raine - otherside", "otherside.ogg", "otherside.png");
        MMDiscButton buttonPigstep = new MMDiscButton(MMDiscButton.MENU, "Pigstep", "Lena Raine - Pigstep", "pigstep.ogg", "pigstep.png");

        buttonsPanel.add(button13); buttonsPanel.add(buttonCat);
        buttonsPanel.add(buttonBlocks); buttonsPanel.add(buttonChirp);
        buttonsPanel.add(buttonFar); buttonsPanel.add(buttonMall);
        buttonsPanel.add(buttonMellohi); buttonsPanel.add(buttonStal);
        buttonsPanel.add(buttonStrad); buttonsPanel.add(buttonWard);
        buttonsPanel.add(button11); buttonsPanel.add(buttonWait);
        buttonsPanel.add(buttonOtherside); buttonsPanel.add(buttonPigstep);

        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        labelsPanel.setPreferredSize(new Dimension(200, 100));

        JLabel modName = new JLabel(String.format("CMDR v%s", MOD_VERSION));
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

        this.add(buttonsPanel, BorderLayout.LINE_START);
        this.add(labelsPanel, BorderLayout.LINE_END);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(defaultDiscIcon.getImage());
        this.setResizable(false);
        this.setSize(640, 480);
        this.setVisible(true);
    }
    
    static void changeDiscLabel(ImageIcon icon, String discFullName) {
        discLabel.setIcon(icon);
        discLabel.setText(discFullName);
    }

    public static void raiseExceptionPane(int state, String message) {
        JOptionPane.showMessageDialog(managerMenu, String.format("Got an exception while converting: %s", message), "CMDR Manager", state);
    }

    static void setDefaultRecord(String record) {
        File recordFile = new File(RECORD_DIR + record);
        if (recordFile.exists()) {
            if (recordFile.delete()) {
                System.out.println(record + ": returned default record successfully.");
            }
            else {
                System.out.println(record + ": failed to return default record.");
            }
        }
    }

    /*
     * Not used code
     */

    /*static void setDifferentDefaultRecord(String initialRecord, String finalRecord) {
        try {
            Files.copy(Paths.get(DEFAULT_RECORDS_DIR + finalRecord), Paths.get(RECORD_DIR + initialRecord), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) { System.out.println(e); }
    }*/

    static void setCustomRecord(String record) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("MP3/WAV audio (*.mp3, *.wav)", "mp3", "wav"));

        int fcOption = fc.showOpenDialog(managerMenu);
        if (fcOption == JFileChooser.APPROVE_OPTION) {
            statusBar.setText("Trying to convert your audio...");
            try {
                AudioConverter.convertToOGG(fc.getSelectedFile().getAbsolutePath(), RECORD_DIR + record);
            } catch (Exception e) {
                System.out.println(e);
                statusBar.setText(" ");
            }
        }
    }

    public static void main(String[] args) {
        RECORD_DIR = args[0]; ICON_DIR = args[1]; SPRITES_DIR = args[2]; MOD_VERSION = args[3];

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                managerMenu = new ManagerMenu();
            }
        });
    }
}
