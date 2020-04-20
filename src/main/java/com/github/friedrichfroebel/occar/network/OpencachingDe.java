package com.github.friedrichfroebel.occar.network;

import com.github.friedrichfroebel.occar.config.Configuration;
import com.github.friedrichfroebel.occar.helper.CachetypesBitmask;
import com.github.friedrichfroebel.occar.helper.Translation;

import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class handles the communication with Opencaching.de
 */
public class OpencachingDe {

    /**
     * The base url for the OKAPI.
     */
    private static final String URL_BASE =
            "https://www.opencaching.de/okapi/services/";

    /**
     * The consumer key for the OKAPI.
     */
    private static final String CONSUMER_KEY = "8YV657YqzqDcVC3QC9wM";

    /**
     * Get the user id for the username saved inside the configuration.
     *
     * @return The user id if the user exists.
     */
    public static String requestUuid() {
        String data;
        try {
            data = RequestBase.getPageContent(URL_BASE
                    + "users/by_username?username=" + Configuration.getUser()
                    + "&fields=uuid&consumer_key=" + CONSUMER_KEY);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null,
                    MessageFormat.format("{0}\n{1}",
                            Translation.getMessage("userNotFound"),
                            Translation.getMessage("checkParameters")),
                    Translation.getMessage("invalidUsername"),
                    JOptionPane.INFORMATION_MESSAGE);
            return "";
        }

        try {
            final JSONObject root = new JSONObject(data);
            final String uuid = root.getString("uuid");
            return uuid;
        } catch (JSONException exception) {
            JOptionPane.showMessageDialog(null,
                    MessageFormat.format("{0}\n{1}",
                            Translation.getMessage("userNotFound"),
                            Translation.getMessage("checkParameters")),
                    Translation.getMessage("invalidUsername"),
                    JOptionPane.INFORMATION_MESSAGE);
            return "";
        }
    }

    /**
     * Request the nearest caches from the given coordinates which the user has
     * not found.
     *
     * @param lat The latitude of the position.
     * @param lon The longitude of the position.
     * @param uuid The user id.
     * @return The JSON response object.
     */
    public static String requestNearestCaches(String lat, String lon,
                                              String uuid) {
        String data = "";
        try {
            data = RequestBase.getPageContent(URL_BASE
                    + "caches/search/nearest?center=" + lat + "|" + lon
                    + "&radius=" + Configuration.getRadius()
                    + "&difficulty=" + Configuration.getDifficulty()
                    + "&terrain=" + Configuration.getTerrain()
                    + "&not_found_by=" + uuid
                    + "&status=Available&consumer_key=" + CONSUMER_KEY);
            if (Configuration.getTypes() != 1023) {
                data = data + "&type=" + CachetypesBitmask.intToQuery(
                        Configuration.getTypes());
            }
        } catch (IOException exception) {
            // pass
        }

        return data;
    }

    /**
     * Request the GPX file for the given cache code query.
     *
     * @param codes A query of cache codes, each separated by a pipe.
     * @param uuid The user id.
     * @param filename The output filename.
     * @throws IOException An error occurred while retrieving the file.
     */
    public static void requestGpxFromCodes(String codes, String uuid,
                                           String filename)
            throws IOException {
        RequestBase.writePageContentToFile(URL_BASE
            + "caches/formatters/gpx?cache_codes=" + codes
            + "&ns_ground=true&latest_logs=true&mark_found=true"
            + "&user_uuid=" + uuid
            + "&consumer_key=" + CONSUMER_KEY,
            filename);
    }
}
