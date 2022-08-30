package me.syberiak.cmdr.util;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class CSVReader {

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
