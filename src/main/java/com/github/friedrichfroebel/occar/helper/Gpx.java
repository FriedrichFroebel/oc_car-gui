package com.github.friedrichfroebel.occar.helper;

import com.github.friedrichfroebel.occar.config.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

/**
 * This class is responsible for parsing GPX files.
 */
public class Gpx {

    /**
     * Convert a GPX route file to a coordinate array.
     *
     * @param distance The distance between two coordinates (only every n-th
     *                 coordinate gets saved).
     * @return The retrieved coordinates.
     * @throws IOException Something went wrong while reading the file.
     */
    public static ArrayList<String> gpx2Array(int distance)
            throws IOException {
        ArrayList<String> coordinates = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                        Configuration.getGpxFile()), "UTF-8"));
        String line;

        // Count the lines to be able to save every n-th coordinate only.
        int lineCounter = 0;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.trim().startsWith("<trkpt lat")) {
                if (lineCounter % distance == 0) {
                    // Avoid out of bounds errors.
                    if (!line.contains(" lat=\"")
                            && !line.contains(" lon=\"")) {
                        continue;
                    }

                    line = line.trim();

                    String lat = line.split(" lat=\"")[1].split("\"")[0];
                    String lon = line.split(" lon=\"")[1].split("\"")[0];

                    coordinates.add(lat + "," + lon);
                }
                lineCounter++;
            }
        }

        bufferedReader.close();

        return coordinates;
    }

    /**
     * Convert a GPX route file to a coordinate array. In this case, every
     * 10-th coordinate is saved.
     *
     * @return The retrieved coordinates.
     * @throws IOException Something went wrong while reading the file.
     */
    public static ArrayList<String> gpx2Array()
            throws IOException {
        return gpx2Array(10);
    }
}
