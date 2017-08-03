package com.github.friedrichfroebel.occar;

import com.github.friedrichfroebel.occar.config.Configuration;
import com.github.friedrichfroebel.occar.frame.ProgressBar;
import com.github.friedrichfroebel.occar.helper.Gpx;
import com.github.friedrichfroebel.occar.helper.Kml;
import com.github.friedrichfroebel.occar.helper.Translation;
import com.github.friedrichfroebel.occar.network.Email;
import com.github.friedrichfroebel.occar.network.OpencachingDe;
import com.github.friedrichfroebel.occar.network.OpenstreetmapOrg;
import com.github.friedrichfroebel.occar.network.YournavigationOrg;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This class performs the search.
 */
class Search {

    /**
     * The user id.
     */
    private static String uuid;

    /**
     * The progress bar.
     */
    private static ProgressBar progressBar;

    /**
     * The coordinates retrieved from the route file.
     */
    private static ArrayList<String> coordinates = new ArrayList<>();

    /**
     * The caches which have been found during the search.
     */
    private static List<String> caches;

    /**
     * Perform the search.
     *
     * @return Success or error message.
     */
    static String performSearch() {
        uuid = OpencachingDe.requestUuid();
        if (uuid.isEmpty()) {
            return Translation.getMessage("userNotFound");
        }

        coordinates.clear();

        if (Configuration.getGpxFile().isEmpty()) {
            return searchKml();
        } else {
            return searchGpx();
        }
    }

    /**
     * Search based on a GPX route.
     *
     * @return Success or error message.
     */
    private static String searchGpx() {
        try {
            coordinates = Gpx.gpx2Array();
        } catch (IOException exception) {
            return Translation.getMessage("errorReadingGpx");
        }

        return searchCommon();
    }

    /**
     * Search based on a KML route.
     *
     * @return Success or error message.
     */
    private static String searchKml() {
        // Request the coordinates for the start.
        String latLonStart = OpenstreetmapOrg.requestLatLonForQuery(
                formatCityName(Configuration.getStart()));
        if (latLonStart.isEmpty()) {
            return Translation.getMessage("errorRetrievingCity");
        }
        String latStart = latLonStart.split(";")[0];
        String lonStart = latLonStart.split(";")[1];

        // Request the coordinates for the destination.
        String latLonDestination = OpenstreetmapOrg.requestLatLonForQuery(
                formatCityName(Configuration.getDestination()));
        if (latLonDestination.isEmpty()) {
            return Translation.getMessage("errorRetrievingCity");
        }
        String latDestination = latLonDestination.split(";")[0];
        String lonDestination = latLonDestination.split(";")[1];

        // Request the route between the two coordinates.
        String kmlRoute = YournavigationOrg.requestKmlRoute(
                latStart, lonStart, latDestination, lonDestination);
        if (kmlRoute.isEmpty()) {
            return Translation.getMessage("errorRetrievingRoute");
        }

        coordinates = Kml.kml2Array(kmlRoute);

        return searchCommon();
    }

    /**
     * The search which is used by both route types after retrieving the
     * coordinates for the waypoints.
     *
     * @return Success or error message.
     */
    private static String searchCommon() {
        // Show the progress bar.
        progressBar = new ProgressBar();
        progressBar.setVisible(true);

        requestCaches();
        return downloadCaches();
    }

    /**
     * Request the caches for all the coordinates.
     */
    private static void requestCaches() {
        caches = new ArrayList<>();

        // Search caches for each coordinate pair.
        final int coordinateSize = coordinates.size();
        for (int i = 0; i < coordinateSize; i++) {
            // Request the nearest caches.
            String[] coordinateParts = coordinates.get(i).split(",");
            String response = OpencachingDe.requestNearestCaches(
                    coordinateParts[0], coordinateParts[1], uuid
            );

            // Update the progress bar.
            progressBar.updateBar((100 * i) / coordinates.size());

            // Check if there are caches inside the response.
            if (response.length() >= 30) {
                // Remove the first JSON part.
                response = response.replace("{\"results\":[", "");

                String[] responseParts = response.split("]")[0].split(",");

                // Add all the caches to the list. Each is added only once.
                for (String responsePart : responseParts) {
                    responsePart = responsePart.replace("\"", "");
                    if (!caches.contains(responsePart)) {
                        caches.add(responsePart);
                    }
                }
            }
        }

        // Close the progress bar.
        progressBar.dispose();
    }

    /**
     * Download all the caches as GPX and send it as email if wished.
     *
     * @return Success or error message.
     */
    private static String downloadCaches() {
        List<List<String>> subcalls = splitList(caches, 490);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");

        boolean successAllEmails = true;

        for (List<String> subcall : subcalls) {
            StringBuilder codesForCall = new StringBuilder(subcall.get(0));
            final int subcallSize = subcall.size();
            for (int i = 1; i < subcallSize; i++) {
                codesForCall.append("|").append(subcall.get(i));
            }

            Date date = new Date();
            String outputFile = System.getProperty("user.home")
                    + File.separator + "occar" + File.separator
                    + dateFormat.format(date) + "PQ.gpx";

            try {
                OpencachingDe.requestGpxFromCodes(codesForCall.toString(),
                        uuid, outputFile);
            } catch (IOException exception) {
                return Translation.getMessage("errorWritingFile");
            }

            if (Configuration.isEmailActive()
                    && !Email.sendEmailWithAttachment(outputFile)) {
                successAllEmails = false;
            }
        }

        if (Configuration.isEmailActive() && !successAllEmails) {
            return Translation.getMessage("errorSendingFile");
        }
        if (Configuration.isEmailActive() && successAllEmails) {
            return Translation.getMessage("successSendingFile");
        }
        return Translation.getMessage("successWritingFile");
    }

    /**
     * Format the city name by replacing all the umlauts and spaces. All
     * characters which are no letters, digits or spaces are removed.
     *
     * @param city The name of the city to format.
     * @return The formatted name which can be passed to the OSM search.
     */
    private static String formatCityName(String city) {
        String name = city.replace("\u00c4", "Ae");
        name = name.replace("\u00e4", "ae");
        name = name.replace("\u00d6", "Oe");
        name = name.replace("\u00f6", "oe");
        name = name.replace("\u00dc", "Ue");
        name = name.replace("\u00fc", "ue");
        name = name.replace("\u00df", "ss");

        name = name.replaceAll("[^A-Za-z0-9\\s]", "");
        name = name.replace(" ", "+");

        return name;
    }

    /**
     * Split a list into a list of sublists with the given length.
     *
     * @param list The list to split.
     * @param length The maximal length of each sublist.
     * @return A list which contains all the sublists.
     */
    private static List<List<String>> splitList(List<String> list, int length) {
        List<List<String>> sublists = new ArrayList<>();
        final int listSize = list.size();
        for (int i = 0; i < listSize; i += length) {
            sublists.add(new ArrayList<>(
                    list.subList(i, Math.min(listSize, i + length))
            ));
        }
        return sublists;
    }
}
