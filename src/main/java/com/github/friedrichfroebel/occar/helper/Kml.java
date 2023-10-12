package com.github.friedrichfroebel.occar.helper;

import java.io.IOException;
import java.io.StringReader;

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
    public static ArrayList<Coordinate> kml2Array(String kmlData,
                                                  int distance) {
        final ArrayList<Coordinate> coordinates = new ArrayList<>();

        Document document;
        try {
            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(
                            new InputSource(new StringReader(kmlData)));
        } catch (ParserConfigurationException | SAXException
                | IOException exception) {
            return coordinates;
        }

        // Coordinates
        final NodeList coordinateTags =
            document.getElementsByTagName("coordinates");
        if (coordinateTags.getLength() != 1) {  // NOPMD
            return coordinates;
        }
        final Node coordinateNode = coordinateTags.item(0);
        if (coordinateNode.getNodeType() != Node.ELEMENT_NODE) {
            return coordinates;
        }
        final Element coordinateElement = (Element) coordinateNode;

        final String[] lines = coordinateNode.getTextContent().split("\n");
        final int lineCount = lines.length;

        for (int i = 0; i < lineCount; i++) {
            final String line = lines[i].trim();

            // Skip invalid entries.
            if (!line.contains(",")) {
                continue;
            }

            if (i % distance != 0) {
                continue;
            }

            // KML: longitude, latitude, and optional altitude.
            final String[] parts = line.split(",");
            coordinates.add(new Coordinate(parts[1], parts[0]));
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
    public static ArrayList<Coordinate> kml2Array(String kmlData) {
        return kml2Array(kmlData, 10);
    }
}
