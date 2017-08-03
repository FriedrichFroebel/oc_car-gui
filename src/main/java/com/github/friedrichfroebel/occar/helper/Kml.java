package com.github.friedrichfroebel.occar.helper;

import java.util.ArrayList;

/**
 * This class is responsible for parsing KML files.
 */
public class Kml {

    /**
     * Convert a KML route file to a coordinate array.
     *
     * @param kmlData The KML route data.
     * @param distance The distance between two coordinates (only every n-th
     *                 coordinate gets saved).
     * @return The retrieved coordinates.
     */
    public static ArrayList<String> kml2Array(String kmlData, int distance) {
        ArrayList<String> coordinates = new ArrayList<>();

        String[] lines = kmlData.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (!line.trim().startsWith("<") && (i % distance == 0)) {
                // Avoid out of bounds errors.
                if (!line.contains(",")) {
                    continue;
                }

                line = line.trim();

                String[] parts = line.split(",");
                coordinates.add(parts[1] + "," + parts[0]);
            }
        }

        return coordinates;
    }

    /**
     * Convert a KML route file to a coordinate array. In this case, every
     * 10-th coordinate is saved.
     *
     * @param kmlData The KML route data.
     * @return The retrieved coordinates.
     */
    public static ArrayList<String> kml2Array(String kmlData) {
        return kml2Array(kmlData, 10);
    }
}
