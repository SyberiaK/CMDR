package me.syberiak.cmdr.ui;

import java.io.File;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ImageIcon;

import me.syberiak.cmdr.CMDR;
public class MMDiscDialog extends JDialog {

    static final int DIALOG_WIDTH = 480;
    static final int DIALOG_HEIGHT = 360;

    public MMDiscDialog(String discFullName, String record, ImageIcon discIcon) {
        super(CMDR.manager, "CMDR Manager", true);
        this.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        
        JLabel l = new JLabel(String.format("Choose what you want to do with \"%s\":", discFullName));
        l.setIcon(new ImageIcon(discIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        l.setPreferredSize(new Dimension(50, 75));
        l.setVerticalAlignment(JLabel.BOTTOM); 
        l.setHorizontalAlignment(JLabel.CENTER);

        JPanel contentPanel = new JPanel(new GridBagLayout());

        JButton setDefaultButton = new JButton("Set default record");
        setDefaultButton.setFocusable(false);
        if (!new File(CMDR.RECORD_DIR + record).exists()) { setDefaultButton.setEnabled(false); }
        JButton setCustomButton = new JButton("Set custom record");
        setCustomButton.setFocusable(false);

        JPanel bottomMargin = new JPanel();
        bottomMargin.setPreferredSize(new Dimension(50, 60));

        setDefaultButton.addActionListener(e -> {
            CMDR.manager.setDefaultRecord(record);
            setDefaultButton.setText("Done!"); 
            setDefaultButton.setEnabled(false);
        });

        setCustomButton.addActionListener(e -> {
            CMDR.manager.setCustomRecord(record);
            setDefaultButton.setText("Set default record");
            if (new File(CMDR.RECORD_DIR + record).exists()) { setDefaultButton.setEnabled(true); }
        });

        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        contentPanel.add(setDefaultButton, c);
        contentPanel.add(setCustomButton, c);

        this.add(l, BorderLayout.PAGE_START);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(bottomMargin, BorderLayout.PAGE_END);

        this.setBounds(ManagerMenu.WINDOW_POSITION_X, ManagerMenu.WINDOW_POSITION_Y,
                       DIALOG_WIDTH, DIALOG_HEIGHT);
        this.setIconImage(discIcon.getImage());
        this.setResizable(false);
        this.setVisible(true);
    }
}
