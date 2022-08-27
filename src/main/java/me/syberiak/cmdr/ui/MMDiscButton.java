package me.syberiak.cmdr.ui;

import java.net.URL;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class MMDiscButton extends JButton {

    public String RECORD;
    public ImageIcon DISC_ICON;
    public String DISC_FULL_NAME;
    public MMDiscButton(String text, String discFullName, String record, URL iconURL) {
        super(text);

        this.RECORD = record;
        this.DISC_ICON = new ImageIcon(iconURL);
        this.DISC_FULL_NAME = discFullName;

        this.addActionListener(e -> new MMDiscDialog(DISC_FULL_NAME, RECORD, DISC_ICON));
        this.addMouseListener(new MMDiscButtonMouseListener());
        this.setFocusable(false);
    }
}