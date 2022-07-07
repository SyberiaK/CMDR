package me.syberiak.cmdr.manager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class MMDiscButtonMouseListener implements MouseListener {

    public void mouseEntered(MouseEvent m) {
        if (m.getSource().getClass() == MMDiscButton.class){
            JButton b = (JButton)m.getSource();
            String[] data = b.getName().split("::");
            if (Integer.valueOf(data[0]) == MMDiscButton.MENU) {
                ManagerMenu.changeDiscLabel(new ImageIcon(ManagerMenu.SPRITES_DIR + data[1]), data[2]);
            } else if (Integer.valueOf(data[0]) == MMDiscButton.DIALOG) {} 
        }
    }

    public void mouseExited(MouseEvent m) {
        if (m.getSource().getClass() == MMDiscButton.class){
            ManagerMenu.changeDiscLabel(ManagerMenu.defaultDiscIcon, "Choose music disc...");
        }
    }
    public void mouseClicked(MouseEvent m) {}

    public void mousePressed(MouseEvent m) {}

    public void mouseReleased(MouseEvent m) {}
}
