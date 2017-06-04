package com.github.friedrichfroebel.occar.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Do bitmask conversions for the cache types.
 */
public class CachetypesBitmask {

    /**
     * The map with all the bitmask values.
     */
    private static Map<Integer, String> bitmaskValues = null;

    /**
     * Init the bitmask map.
     */
    private static void initBitmaskValues() {
        bitmaskValues = new HashMap<>();
        bitmaskValues.put((1 << 0), "Traditional");
        bitmaskValues.put((1 << 1), "Multi");
        bitmaskValues.put((1 << 2), "Quiz");
        bitmaskValues.put((1 << 3), "Virtual");
        bitmaskValues.put((1 << 4), "Event");
        bitmaskValues.put((1 << 5), "Webcam");
        bitmaskValues.put((1 << 6), "Moving");
        bitmaskValues.put((1 << 7), "Math/Physics");
        bitmaskValues.put((1 << 8), "Drive-In");
        bitmaskValues.put((1 << 9), "Other");

    }

    /**
     * Convert an integer cache types value to a query string where the short
     * cache type names are separated by pipes.
     *
     * @param value The cache types value.
     * @return The query string.
     */
    public static String intToQuery(int value) {
        // Init bitmask.
        if (bitmaskValues == null) {
            initBitmaskValues();
        }

        StringBuilder query = new StringBuilder();

        // Perform the check for each bitmask.
        for (Map.Entry<Integer, String> entry : bitmaskValues.entrySet()) {
            if ((value & entry.getKey()) == entry.getKey()) {
                if (query.length() > 0) {
                    query.append("|");
                }
                query.append(entry.getValue());
            }
        }

        return query.toString();
    }

    /**
     * Convert boolean values to an integer value which corresponds to them.
     *
     * @param tradi Whether to add the traditional cache or not.
     * @param multi Whether to add the multicache or not.
     * @param mystery Whether to add the mystery cache or not.
     * @param virtual Whether to add the virtual cache or not.
     * @param event Whether to add the event cache or not.
     * @param webcam Whether to add the webcam cache or not.
     * @param moving Whether to add the moving cache or not.
     * @param math Whether to add the math/physics cache or not.
     * @param drive Whether to add the drive-in cache or not.
     * @param other Whether to add other caches or not.
     * @return The calculated integer value.
     */
    public static int booleanToInt(boolean tradi, boolean multi,
                                   boolean mystery, boolean virtual,
                                   boolean event, boolean webcam,
                                   boolean moving, boolean math,
                                   boolean drive, boolean other) {
        // Init bitmask.
        if (bitmaskValues == null) {
            initBitmaskValues();
        }

        int value = 0;

        // Perform the check for each boolean value.
        for (Map.Entry<Integer, String> entry : bitmaskValues.entrySet()) {
            if (entry.getValue().equals("Traditional") && tradi) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Multi") && multi) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Quiz") && mystery) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Virtual") && virtual) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Event") && event) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Webcam") && webcam) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Moving") && moving) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Math/Physics") && math) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Drive-In") && drive) {
                value += entry.getKey();
            }
            if (entry.getValue().equals("Other") && other) {
                value += entry.getKey();
            }
        }
        return value;
    }
}
