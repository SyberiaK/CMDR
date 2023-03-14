package me.syberiak.cmdr.ui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.*;

import me.syberiak.cmdr.CMDR;

public class MMAboutDialog extends JDialog {

    static final int DIALOG_WIDTH = 480;
    static final int DIALOG_HEIGHT = 360;

    public MMAboutDialog() {
        super(CMDR.manager, "CMDR Manager", true);

        if (CMDR.ICON_URL != null) {
            ImageIcon discImageIcon = new ImageIcon(CMDR.ICON_URL);
            this.setIconImage(discImageIcon.getImage());
        }

        setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();

        JPanel textPanel = new JPanel(new GridBagLayout());

        JLabel authorLabel = new JLabel("Author: SyberiaK");
        authorLabel.setFont(new Font(null, Font.BOLD, 12));

        JTextArea majorChangelog = new JTextArea(String.join("\n", CMDR.MAJOR_CHANGELOG));
        majorChangelog.setWrapStyleWord(true);
        majorChangelog.setLineWrap(true);
        majorChangelog.setEditable(false);
        majorChangelog.setFocusable(false);
        majorChangelog.setBackground(UIManager.getColor("Label.background"));
        majorChangelog.setFont(UIManager.getFont("Label.font"));
        majorChangelog.setBorder(UIManager.getBorder("Label.border"));

        JTextArea latestChangelog = new JTextArea(String.join("\n", CMDR.LATEST_CHANGELOG));
        latestChangelog.setWrapStyleWord(true);
        latestChangelog.setLineWrap(true);
        latestChangelog.setEditable(false);
        latestChangelog.setFocusable(false);
        latestChangelog.setBackground(UIManager.getColor("Label.background"));
        latestChangelog.setFont(UIManager.getFont("Label.font"));
        latestChangelog.setBorder(UIManager.getBorder("Label.border"));

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        JPanel buttonMargin = new JPanel();

        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(50, 25));
        closeButton.setFocusable(false);

        closeButton.addActionListener(e -> this.dispose());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 30, 10, 30);
        c.gridx = 0;
        c.weightx = 1;
        c.gridy = 0;
        textPanel.add(authorLabel, c);

        c.gridwidth = 2;
        c.gridy = 1;
        textPanel.add(majorChangelog, c);
        c.gridx = 2;
        textPanel.add(latestChangelog, c);

        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = GridBagConstraints.RELATIVE;

        c.ipadx = 300;
        buttonPanel.add(buttonMargin, c);

        c.ipadx = 0;
        buttonPanel.add(closeButton, c);

        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);

        setLocation(ManagerMenu.WINDOW_POSITION);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
        setVisible(true);
    }
}
