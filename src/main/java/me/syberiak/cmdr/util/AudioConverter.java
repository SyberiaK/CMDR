package me.syberiak.cmdr.util;

import java.io.File;
import javax.swing.JOptionPane;

import me.syberiak.cmdr.manager.ManagerMenu;
import me.syberiak.cmdr.util.jave.Encoder;
import me.syberiak.cmdr.util.jave.AudioAttributes;
import me.syberiak.cmdr.util.jave.EncoderException;
import me.syberiak.cmdr.util.jave.EncodingAttributes;

public class AudioConverter {
    public static void convertToOGG(String source, String target) {
        Runnable r = ()->{
            AudioAttributes oggAudioAttrs = new AudioAttributes();
            oggAudioAttrs.setBitRate(196000);
            oggAudioAttrs.setSamplingRate(44100);
            //oggAudioAttrs.setChannels(1); // TODO: can't convert to mono because libvorbis and vorbis only supports stereo
            oggAudioAttrs.setQuality(100);
            oggAudioAttrs.setCodec("libvorbis");

            EncodingAttributes oggEncoAttrs = new EncodingAttributes();
            oggEncoAttrs.setFormat("ogg");
            oggEncoAttrs.setAudioAttributes(oggAudioAttrs);

            Encoder encoder = new Encoder();
            try {
                encoder.encode(new File(source), new File(target), oggEncoAttrs);
            } catch(EncoderException e) {
                ManagerMenu.raiseExceptionPane(JOptionPane.WARNING_MESSAGE, e.toString());
                System.out.println("Convertion failed: " + e);
                ManagerMenu.statusBar.setText("");
            }
            System.out.println("Converted successfully.");
            ManagerMenu.statusBar.setText("Done!");
        };
        Thread convertThread = new Thread(r, "convertThread");
        convertThread.start();
    }
}