package com.github.friedrichfroebel.occar.network;

import java.io.IOException;

/**
 * This class handles the communcation with Openstreetmap.org.
 */
public class OpenstreetmapOrg {

    /**
     * The base url for the API.
     */
    private static final String URL_BASE =
            "https://nominatim.openstreetmap.org/";

    /**
     * Request the latitude and longitude for the given query string.
     *
     * @param query The query to perform.
     * @return A string with the latitude and longitude separated by a
     *         semicolon, an empty string if there have been errors.
     */
    public static String requestLatLonForQuery(String query) {
        String data;
        try {
            data = RequestBase.getPageContent(URL_BASE + "search?q="
                    + query + "&format=json");
        } catch (IOException exception) {
            return "";
        }

        return jsonToLatLon(data);
    }

    /**
     * Retrieve the latitude and longitude from a JSON string.
     *
     * @param json The JSON string to parse.
     * @return A string with the latitude and longitude separated by a
     *         semicolon, an empty string if there have been errors.
     */
    private static String jsonToLatLon(String json) {
        try {
            String lat = json.split("\"lat\":\"")[1].split("\"")[0];
            String lon = json.split("\"lon\":\"")[1].split("\"")[0];

            return lat + ";" + lon;
        } catch (ArrayIndexOutOfBoundsException exception) {
            return "";
        }
    }
}
