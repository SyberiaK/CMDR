package me.syberiak.cmdr.ui.component;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.ui.MMDiscDialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class MMDiscButton extends JButton {
    public String RECORD;
    public ImageIcon DISC_ICON;
    public String DISC_FULL_NAME;

    public MMDiscButton(String text, String discFullName, String record, URL iconURL) {
        super(text);

        RECORD = record;
        DISC_ICON = new ImageIcon(iconURL);
        DISC_FULL_NAME = discFullName;

        addActionListener(e -> new MMDiscDialog(DISC_FULL_NAME, RECORD, DISC_ICON));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                CMDR.manager.changeDiscLabel(DISC_ICON, DISC_FULL_NAME);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                CMDR.manager.changeDiscLabel(CMDR.manager.defaultDiscIcon, "Choose music disc...");
            }
        });
        setFocusable(false);
    }
}