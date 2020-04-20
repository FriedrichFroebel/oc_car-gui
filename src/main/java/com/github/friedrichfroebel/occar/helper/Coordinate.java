package com.github.friedrichfroebel.occar.helper;

/**
 * This class encapsulates a simple coordinate.
 */
public class Coordinate {

    /**
     * The latitude.
     */
    private String lat;

    /**
     * The longitude.
     */
    private String lon;

    /**
     * Set the values.
     *
     * @param lat The latitude to set.
     * @param lon The longitude to set.
     */
    public Coordinate(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Get the latitude.
     *
     * @return The latitude.
     */
    public String getLat() {
        return lat;
    }

    /**
     * Get the longitude.
     *
     * @return The longitude.
     */
    public String getLon() {
        return lon;
    }
}
