package me.syberiak.cmdr.util;

import java.io.File;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

public class OGGAudioConverter implements Runnable {
    private volatile boolean success;
    private volatile Exception exception;

    String source;
    String target;

    OGGAudioConverter(String source, String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void run() {
        AudioAttributes oggAudioAttrs = new AudioAttributes();
        oggAudioAttrs.setBitRate(196000);
        oggAudioAttrs.setSamplingRate(44100);
        oggAudioAttrs.setChannels(1);
        oggAudioAttrs.setQuality(100);
        oggAudioAttrs.setCodec("libvorbis");

        EncodingAttributes oggEncoAttrs = new EncodingAttributes();
        oggEncoAttrs.setOutputFormat("ogg");
        oggEncoAttrs.setAudioAttributes(oggAudioAttrs);

        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(new File(source)), new File(target), oggEncoAttrs);
        } catch (EncoderException e) {
            exception = e;
            success = false;
        }
        System.out.println(source + ": converted successfully to " + target);
        success = true;
    }

    Pair<Boolean, Exception> getResult() { return new Pair<>(success, exception); }
    public static Pair<Boolean, Exception> convert(String source, String target) throws InterruptedException {
        if (!(source.endsWith(".mp3") || source.endsWith(".wav"))) {
            return new Pair<>(false,
                    new Exception("Wrong source file extension (only .mp3 and .wav supported)"));
        }
        OGGAudioConverter ac = new OGGAudioConverter(source, target);
        Thread convertThread = new Thread(ac);

        convertThread.start();
        convertThread.join();

        return ac.getResult();
    }
}