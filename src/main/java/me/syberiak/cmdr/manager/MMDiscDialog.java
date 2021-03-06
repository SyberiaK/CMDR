package me.syberiak.cmdr.manager;

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

public class MMDiscDialog extends JDialog {

    static JButton setDefaultButton;

    public MMDiscDialog(String discFullName, String record, String discIcon) {
        super(ManagerMenu.managerMenu, "CMDR Manager", true);
        ImageIcon discImageIcon = new ImageIcon(ManagerMenu.SPRITES_DIR + discIcon);
        this.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        
        JLabel l = new JLabel(String.format("Choose what you want to do with \"%s\":", discFullName));
        l.setIcon(new ImageIcon(discImageIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        l.setPreferredSize(new Dimension(50, 75));
        l.setVerticalAlignment(JLabel.BOTTOM); 
        l.setHorizontalAlignment(JLabel.CENTER);

        JPanel contentPanel = new JPanel(new GridBagLayout());

        setDefaultButton = new JButton("Set default record");
        setDefaultButton.setFocusable(false);
        if (!new File(ManagerMenu.RECORD_DIR + record).exists()) {setDefaultButton.setEnabled(false);}
        /*JButton setDifferentDefaultButton = new JButton("Set different default record");
        setDifferentDefaultButton.setFocusable(false);*/
        JButton setCustomButton = new JButton("Set custom record");
        setCustomButton.setFocusable(false);

        JPanel bottomSpace = new JPanel();
        bottomSpace.setPreferredSize(new Dimension(50, 60));

        setDefaultButton.addActionListener(e -> {
            ManagerMenu.setDefaultRecord(record); 
            setDefaultButton.setText("Done!"); 
            setDefaultButton.setEnabled(false);
        });

        //setDifferentDefaultButton.addActionListener(e -> {new MMChooseDefaultDialog(discFullName, record, discIcon);});

        setCustomButton.addActionListener(e -> {
            ManagerMenu.setCustomRecord(record);
            setDefaultButton.setText("Set default record");
            setDefaultButton.setEnabled(true);});

        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        contentPanel.add(setDefaultButton, c);
        //contentPanel.add(setDifferentDefaultButton, c);
        contentPanel.add(setCustomButton, c);

        this.add(l, BorderLayout.PAGE_START);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(bottomSpace, BorderLayout.PAGE_END);

        this.setBounds(100, 100, 480, 360);
        this.setIconImage(discImageIcon.getImage());
        this.setResizable(false);
        this.setVisible(true);
    }
}
