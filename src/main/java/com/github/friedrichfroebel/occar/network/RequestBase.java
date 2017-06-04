package com.github.friedrichfroebel.occar.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * This is the basic request class.
 */
class RequestBase {

    /**
     * Get the content of the given website.
     *
     * @param urlString The website to get the data from.
     * @return The data of the website.
     * @throws IOException An error occurred while retrieving the data.
     */
    static String getPageContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        InputStream inputStream = url.openStream();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"));

        StringBuilder content = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }

        inputStream.close();
        bufferedReader.close();

        return content.toString();
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
        URL url = new URL(urlString);
        File file = new File(filepath);
        FileUtils.copyURLToFile(url, file);
    }
}
