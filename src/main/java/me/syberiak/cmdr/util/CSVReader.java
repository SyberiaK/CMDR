package me.syberiak.cmdr.util;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class CSVReader {

    public static List<String[]> readDataFromCSV(String filename, String delimiter) throws IOException {
        return readDataFromCSV(Paths.get(new File(filename).getAbsolutePath()), delimiter);
    }

    public static List<String[]> readDataFromCSV(Path filepath, String delimiter) throws IOException {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(filepath,
                StandardCharsets.US_ASCII)) {

            String line = br.readLine();

            while (line != null) {
                String[] attributes = line.split(delimiter);
                data.add(attributes);

                line = br.readLine();
            }
        }

        return data;
    }

    public static List<String[]> readDataFromCSV(InputStream inputStream, String delimiter) throws IOException {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line = br.readLine();

            while (line != null) {
                String[] attributes = line.split(delimiter);
                data.add(attributes);

                line = br.readLine();
            }
        }

        return data;
    }
}
