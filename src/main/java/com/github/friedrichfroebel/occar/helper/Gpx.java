package com.github.friedrichfroebel.occar.helper;

import com.github.friedrichfroebel.occar.config.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
    public static ArrayList<Coordinate> gpx2Array(int distance)
            throws IOException {
        final ArrayList<Coordinate> coordinates = new ArrayList<>();

        Document document;
        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(
                        new InputSource(new InputStreamReader(
                            new FileInputStream(
                                Configuration.getGpxFile()),
                                "UTF-8")));
        } catch (ParserConfigurationException | SAXException
                | IOException exception) {
            return coordinates;
        }

        // Track points.
        final NodeList trkpts = document.getElementsByTagName("trkpt");
        final int pointCount = trkpts.getLength();
        int pointCounter = 0;
        for (int i = 0; i < pointCount; i++) {
            final Node trkpt = trkpts.item(i);
            if (trkpt.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (pointCounter % distance != 0) {
                pointCounter++;
                continue;
            }
            final Element elementNode = (Element) trkpt;
            final String lat = elementNode.getAttribute("lat");
            final String lon = elementNode.getAttribute("lon");
            coordinates.add(new Coordinate(lat, lon));
            pointCounter++;
        }

        return coordinates;
    }

    /**
     * Convert a GPX route file to a coordinate array. In this case, every
     * 10-th coordinate is saved.
     *
     * @return The retrieved coordinates.
     * @throws IOException Something went wrong while reading the file.
     */
    public static ArrayList<Coordinate> gpx2Array()
            throws IOException {
        return gpx2Array(10);
    }
}
