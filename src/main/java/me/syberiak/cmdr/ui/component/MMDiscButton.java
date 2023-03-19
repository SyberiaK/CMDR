package me.syberiak.cmdr.ui.component;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.ui.MMDiscDialog;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class MMDiscButton extends JButton {
    public String record;
    public ImageIcon icon;
    public String discFullName;

    public MMDiscButton(String text, String discFullName, String record, URL iconURL) {
        super(text);

        this.record = record;
        this.icon = new ImageIcon(iconURL);
        this.discFullName = discFullName;

        addActionListener(e -> new MMDiscDialog(discFullName, record, icon));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                CMDR.manager.changeDiscLabel(icon, discFullName);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                CMDR.manager.changeDiscLabel(CMDR.manager.defaultDiscIcon, "Choose music disc...");
            }
        });
        setFocusable(false);
    }
}