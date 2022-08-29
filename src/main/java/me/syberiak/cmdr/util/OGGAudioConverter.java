package me.syberiak.cmdr.util;

import java.io.File;
import java.util.Arrays;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

public class OGGAudioConverter implements Runnable {

    String source;
    String target;

    OGGAudioConverter(String source, String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void run() {
        AudioAttributes audioAttrs = new AudioAttributes();
        audioAttrs.setBitRate(196000);
        audioAttrs.setSamplingRate(44100);
        audioAttrs.setChannels(1);
        audioAttrs.setQuality(100);
        audioAttrs.setCodec("libvorbis");

        EncodingAttributes encoderAttrs = new EncodingAttributes();
        encoderAttrs.setOutputFormat("ogg");
        encoderAttrs.setAudioAttributes(audioAttrs);

        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(new File(source)), new File(target), encoderAttrs);
        } catch (EncoderException e) {
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, e);
        }
    }

    public static void convert(String source, String target) throws Exception {
        String[] formats = {"mp3", "wav", "ogg"};

        boolean isCorrectFormat = Arrays.asList(formats).contains(
                source.substring(source.lastIndexOf('.') + 1));

        if (!isCorrectFormat) {
            throw new Exception(String.format("Wrong source file extension of %s" +
                            " (.mp3, .wav and .ogg supported)", source));
        }

        OGGAudioConverter ac = new OGGAudioConverter(source, target);
        Thread convertThread = new Thread(ac);

        convertThread.start();
        convertThread.join();
    }
}