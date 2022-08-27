package me.syberiak.cmdr.ui;

import javax.swing.*;
import java.net.URL;
import java.util.Objects;

public class MMDiscButton extends JButton {
    static String MENU = "MMDiscButton MENU";
    // static String DIALOG = "MMDiscButton DIALOG";

    public String PARENT;
    public String RECORD;
    public ImageIcon DISC_ICON;
    public String DISC_FULL_NAME;
    public MMDiscButton(String parent, String text, String discFullName, String record, URL iconURL) {
        super(text);

        this.PARENT = parent;
        this.RECORD = record;
        this.DISC_ICON = new ImageIcon(iconURL);
        this.DISC_FULL_NAME = discFullName;

        if (Objects.equals(PARENT, MENU)) {
            this.addActionListener(e -> new MMDiscDialog(DISC_FULL_NAME, RECORD, DISC_ICON));
        } /*else if (PARENT == DIALOG) {
            String initialRecord = MMChooseDefaultDialog.initialRecord;
            if (initialRecord == RECORD) {
                this.setEnabled(false);
            } else {
                this.addActionListener(e -> {
                    ManagerMenu.setDifferentDefaultRecord(MMChooseDefaultDialog.initialRecord, RECORD);
                    MMDiscDialog.setDefaultButton.setEnabled(true);
                });
            }
        }*/
        this.addMouseListener(new MMDiscButtonMouseListener());
        this.setFocusable(false);
    }
}