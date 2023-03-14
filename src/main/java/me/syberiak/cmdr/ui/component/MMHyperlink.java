package me.syberiak.cmdr.ui.component;

import me.syberiak.cmdr.CMDR;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

public class MMHyperlink extends JLabel {
    private String url;
    private static final String html = "<html><a href=''>%s</a></html>";

    public MMHyperlink(String text) {
        this(text, null, null);
    }

    public MMHyperlink(String text, String url) {
        this(text, url, null);
    }

    public void setURL(String url) {
        this.url = url;
    }

    public MMHyperlink(String text, String url, String tooltip) {
        super(text);
        this.url = url;

        setForeground(Color.BLUE);

        setToolTipText(tooltip);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setText(String.format(html, text));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setText(text);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(MMHyperlink.this.url));
                } catch (IOException | URISyntaxException e1) {
                    CMDR.LOGGER.error("Redirecting to changelog failed! Reason: " + e1);
                }
            }

        });
    }
}