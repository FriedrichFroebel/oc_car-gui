package com.github.friedrichfroebel.occar.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URI;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * This is the basic request class.
 */
class RequestBase {

    /**
     * Convert the given URL string to an actual URL object.
     *
     * @param urlString The URL string to convert.
     * @return The generated URL object.
     * @throws IOException The string could not be converted.
     */
    static URL convertStringToUrl(String urlString) throws IOException {
        URI uri = URI.create(urlString);
        return uri.toURL();
    }

    /**
     * Get the content of the given website.
     *
     * @param urlString The website to get the data from.
     * @return The data of the website.
     * @throws IOException An error occurred while retrieving the data.
     */
    static String getPageContent(String urlString) throws IOException {
        final URL url = convertStringToUrl(urlString);
        try (InputStream inputStream = url.openStream()) {
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"))) {
                final StringBuilder content = new StringBuilder();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    content.append(line).append("\n");
                }
                return content.toString();
            }
        }
    }

    /**
     * Write the content of the given website to the given file.
     *
     * @param urlString The website to get the data from.
     * @param filepath The file to write the data to.
     * @throws IOException An error occurred while retrieving the data or
     *                     writing to the file.
     */
    static void writePageContentToFile(String urlString, String filepath)
            throws IOException {
        final URL url = convertStringToUrl(urlString);
        File file = new File(filepath);
        FileUtils.copyURLToFile(url, file);
    }
}
