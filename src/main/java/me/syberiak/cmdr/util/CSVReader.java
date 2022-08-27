package me.syberiak.cmdr.util;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class CSVReader {

    public static List<String[]> readDataFromCSV(String filename, String delimiter) {
        return readDataFromCSV(Paths.get(new File(filename).getAbsolutePath()), delimiter);
    }

    public static List<String[]> readDataFromCSV(Path filepath, String delimiter) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(filepath,
                StandardCharsets.US_ASCII)) {

            String line = br.readLine();

            while (line != null) {
                String[] attributes = line.split(delimiter);
                data.add(attributes);

                line = br.readLine();
            }
        } catch (IOException e) { e.printStackTrace(); }

        return data;
    }
}
