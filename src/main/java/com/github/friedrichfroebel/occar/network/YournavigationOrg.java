package com.github.friedrichfroebel.occar.network;

import java.io.IOException;

/**
 * This class handles the communcation with Yournavigation.org.
 */
public class YournavigationOrg {

    /**
     * The base url for the API.
     */
    private static final String URL_BASE =
            "http://www.yournavigation.org/api/1.0/gosmore.php";

    /**
     * Request a KML route for the given points.
     *
     * @param latStart The latitude of the start.
     * @param lonStart The longitude of the start.
     * @param latDestination The latitude of the destination.
     * @param lonDestination The longitude of the destination.
     * @return The KML route as a string, an empty string if there have been
     *         errors.
     */
    public static String requestKmlRoute(String latStart, String lonStart,
                                         String latDestination,
                                         String lonDestination) {
        String data;
        try {
            data = RequestBase.getPageContent(URL_BASE + "?flat="
                    + latStart + "&flon=" + lonStart + "&tlat="
                    + latDestination + "&tlon=" + lonDestination
                    + "&v=motorcar&fast=1");
        } catch (IOException exception) {
            return "";
        }

        return data;
    }
}
