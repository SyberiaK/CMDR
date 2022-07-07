package me.syberiak.cmdr.manager;

import javax.swing.JButton;

public class MMDiscButton extends JButton {
    static int MENU = 8;
    static int DIALOG = 16;

    public MMDiscButton(int parent, String text, String discFullName, String record, String discIcon) {
        super(text);
        this.setName(String.format("%s::%s::%s", parent, discIcon, discFullName));
        if (parent == MENU) {
            this.addActionListener(e -> {new MMDiscDialog(discFullName, record, discIcon);});
        } /*else if (parent == DIALOG) {
            String initialRecord = MMChooseDefaultDialog.initialRecord;
            if (initialRecord == record) {
                this.setEnabled(false);
            } else {
                this.addActionListener(e -> {
                    ManagerMenu.setDifferentDefaultRecord(MMChooseDefaultDialog.initialRecord, record);
                    MMDiscDialog.setDefaultButton.setEnabled(true);
                });
            }
        }*/
        this.addMouseListener(new MMDiscButtonMouseListener());
        this.setFocusable(false);
    }
}