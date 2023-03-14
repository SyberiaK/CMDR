package me.syberiak.cmdr.ui;

import me.syberiak.cmdr.CMDR;
import me.syberiak.cmdr.ui.component.MMHyperlink;

import javax.swing.*;
import java.awt.event.*;

public class ReworkedMMAboutDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonClose;
    private JLabel versionLabel;
    private MMHyperlink changelogLinkLabel;

    public ReworkedMMAboutDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonClose);

        pack();
        setLocation(ManagerMenu.WINDOW_POSITION);
        setResizable(false);
        setVisible(true);

        buttonClose.addActionListener(e -> onClose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onClose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onClose() {
        dispose();
    }

    private void createUIComponents() {
        ImageIcon icon = CMDR.manager.defaultDiscIcon;
        if (icon != null) {
            setIconImage(icon.getImage());
        }
        setTitle("CMDR Manager");

        versionLabel = new JLabel(CMDR.VERSION_V);
        String changelogLink = "https://github.com/SyberiaK/CMDR/releases/tag/" + CMDR.VERSION_V;

        changelogLinkLabel = new MMHyperlink("What's New?",
                changelogLink,
                String.format("This will send you to changelog page on Github (%s)", changelogLink));
    }
}
