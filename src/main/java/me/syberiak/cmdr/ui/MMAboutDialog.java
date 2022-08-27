package me.syberiak.cmdr.ui;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ImageIcon;

import me.syberiak.cmdr.CMDR;

public class MMAboutDialog extends JDialog {

    public MMAboutDialog() {
        super(CMDR.manager, "CMDR Manager", true);

        if (CMDR.ICON_URL != null) {
            ImageIcon discImageIcon = new ImageIcon(CMDR.ICON_URL);
            this.setIconImage(discImageIcon.getImage());
        }
        this.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();

        JPanel textPanel = new JPanel(new GridBagLayout());

        JLabel authorLabel = new JLabel("Author: SyberiaK");

        JLabel detailedLabel = new JLabel("Someday there will be a lot more info...");

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        JPanel buttonMargin = new JPanel();

        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(50, 25));
        closeButton.setFocusable(false);

        closeButton.addActionListener(e -> this.dispose());

        c.insets = new Insets(0, 0, 10, 0);
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        textPanel.add(authorLabel, c);

        c.gridwidth = 2;
        textPanel.add(detailedLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = GridBagConstraints.RELATIVE;
        c.ipadx = 200;
        buttonPanel.add(buttonMargin, c);

        c.ipadx = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(0, 0, 10, 10);
        c.gridwidth = 1;
        buttonPanel.add(closeButton, c);

        this.add(textPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.PAGE_END);
        this.setBounds(100, 100, 480, 360);
        this.setResizable(false);
        this.setVisible(true);
    }
}
