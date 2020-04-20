package com.github.friedrichfroebel.occar.network;

import com.github.friedrichfroebel.occar.helper.Coordinate;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static Coordinate requestLatLonForQuery(String query) {
        String data;
        try {
            data = RequestBase.getPageContent(URL_BASE + "search?q="
                    + query + "&format=json");
        } catch (IOException exception) {
            return null;
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
    private static Coordinate jsonToLatLon(String json) {
        try {
            final JSONArray root = new JSONArray(json);
            final JSONObject first = root.getJSONObject(0);
            final String lat = first.getString("lat");
            final String lon = first.getString("lon");
            return new Coordinate(lat, lon);
        } catch (JSONException exception) {
            return null;
        }
    }
}
