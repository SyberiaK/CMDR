package me.syberiak.cmdr.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import me.syberiak.cmdr.CMDR;

public class MMDiscButtonMouseListener implements MouseListener {

    public void mouseEntered(MouseEvent m) {
        if (m.getSource().getClass() == MMDiscButton.class) {
            MMDiscButton b = (MMDiscButton)m.getSource();

            CMDR.manager.changeDiscLabel(b.DISC_ICON, b.DISC_FULL_NAME);
        }
    }

    public void mouseExited(MouseEvent m) {
        if (m.getSource().getClass() == MMDiscButton.class) {
            CMDR.manager.changeDiscLabel(CMDR.manager.defaultDiscIcon, "Choose music disc...");
        }
    }

    public void mouseClicked(MouseEvent m) { }
    public void mousePressed(MouseEvent m) { }
    public void mouseReleased(MouseEvent m) { }
}
