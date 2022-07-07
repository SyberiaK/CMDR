package me.syberiak.cmdr.manager;

/*
 * Not used code
 */

/*import java.awt.Image;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.ImageIcon;

public class MMChooseDefaultDialog extends JDialog {

    static String initialRecord;

    public MMChooseDefaultDialog(String discFullName, String record, String discIcon) {
        super(ManagerMenu.managerMenu, "CMDR Manager", true);
        ImageIcon discImageIcon = new ImageIcon(ManagerMenu.SPRITES_DIR + discIcon);
        this.setLayout(new BorderLayout());

        initialRecord = record;

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        

        JLabel l = new JLabel(String.format("Choose a record you want it to replace \"%s\":", discFullName));
        l.setIcon(new ImageIcon(discImageIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        l.setPreferredSize(new Dimension(50, 75));
        l.setVerticalAlignment(JLabel.BOTTOM); 
        l.setHorizontalAlignment(JLabel.CENTER);

        JPanel contentPanel = new JPanel(new GridBagLayout());

        MMDiscButton button11 = new MMDiscButton(MMDiscButton.DIALOG, "11", "C418 - 11", "11.ogg", "11.png");
        MMDiscButton button13 = new MMDiscButton(MMDiscButton.DIALOG, "13", "C418 - 13", "13.ogg", "13.png");
        MMDiscButton buttonCat = new MMDiscButton(MMDiscButton.DIALOG, "сat", "C418 - cat", "cat.ogg", "cat.png");
        MMDiscButton buttonBlocks = new MMDiscButton(MMDiscButton.DIALOG, "blocks", "C418 - blocks", "blocks.ogg", "blocks.png");

        JPanel bottomSpace = new JPanel();
        bottomSpace.setPreferredSize(new Dimension(50, 60));
   
        contentPanel.add(button11, c);
        contentPanel.add(button13, c);
        c.gridy = 1;
        contentPanel.add(buttonCat, c);
        contentPanel.add(buttonBlocks, c);
        
        this.add(l, BorderLayout.PAGE_START);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(bottomSpace, BorderLayout.PAGE_END);

        this.setBounds(100, 100, 480, 360);
        this.setIconImage(discImageIcon.getImage());
        this.setResizable(false);
        this.setVisible(true);
    }
}*/