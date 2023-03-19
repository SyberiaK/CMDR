package me.syberiak.cmdr.util;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

public class OGGAudioConverter implements Runnable {

    public static String[] SUPPORTED_FORMATS = {"mp3", "wav", "ogg"};

    static EncodingAttributes encoderAttrs;
    static {
        AudioAttributes audioAttrs = new AudioAttributes();
        audioAttrs.setBitRate(196000);
        audioAttrs.setSamplingRate(44100);
        audioAttrs.setChannels(1);
        audioAttrs.setQuality(100);
        audioAttrs.setCodec("libvorbis");

        encoderAttrs = new EncodingAttributes();
        encoderAttrs.setOutputFormat("ogg");
        encoderAttrs.setAudioAttributes(audioAttrs);
    }
    File source;
    File target;



    OGGAudioConverter(File source, File target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void run() {
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, encoderAttrs);
        } catch (EncoderException e) {
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, e);
        }
    }

    public static void convert(File source, File target) throws Exception {
        boolean isCorrectSourceFormat = Arrays.asList(SUPPORTED_FORMATS)
                .contains(FilenameUtils.getExtension(source.toString()));
        boolean isCorrectTargetFormat = source.toPath().endsWith(".ogg");

        if (!isCorrectSourceFormat) {
            throw new Exception(String.format("Wrong source file extension of %s" +
                            " (.mp3, .wav and .ogg supported)", source));
        }
        if (!isCorrectTargetFormat) {
            throw new Exception(String.format("Wrong target file extension of %s" +
                    " (must be .ogg)", target));
        }

        OGGAudioConverter ac = new OGGAudioConverter(source, target);
        Thread convertThread = new Thread(ac);

        convertThread.start();
        convertThread.join();
    }
}