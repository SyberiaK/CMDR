package me.syberiak.cmdr.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class CSVReader {

    public static List<String[]> readDataFromCSV(InputStream inputStream, String delimiter) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String[]> data = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] attributes = line.split(delimiter);
            data.add(attributes);
        }

        return data;
    }
}
