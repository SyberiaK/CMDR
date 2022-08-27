package me.syberiak.cmdr.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

public class StreamUtil {

    static final String PREFIX = "streamToFile";
    static final String SUFFIX = ".tmp";

    public static File streamToFile (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        return tempFile;
    }
}