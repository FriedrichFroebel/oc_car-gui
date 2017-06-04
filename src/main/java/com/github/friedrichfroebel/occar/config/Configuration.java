package com.github.friedrichfroebel.occar.config;

import com.github.friedrichfroebel.occar.helper.Translation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * This class holds all the configuration values.
 */
public final class Configuration {

    /**
     * The path of the configuration file.
     */
    private static final String FILEPATH =
            System.getProperty("user.home") + File.separator + "occar"
                    + File.separator + "occar_config.xml";

    /**
     * The properties element which is used to read from and write to the
     * configuration file.
     */
    private static Properties config = new Properties();

    /**
     * The map which is the internal data structure for holding all the
     * configuration values.
     */
    private static Map<String, String> values = new HashMap<>();

    /**
     * Status variable to save the email password if needed. {@code False} by
     * default to avoid that an empty password overwrites a saved password on
     * startup.
     */
    private static boolean saveEmailPassword = false;

    /**
     * The path to the GPX route file to load.
     */
    private static String gpxLoadPath = "";

    /**
     * Read the values from the configuration file.
     */
    public static void readConfig() {
        File file = new File(FILEPATH);

        // Write the configuration file if it does not exist.
        if (!file.exists()) {
            writeConfig();
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            config.loadFromXML(inputStream);

            values.put("ocUser",
                    config.getProperty("ocUser", "User"));
            values.put("radius",
                    config.getProperty("radius", "2"));
            values.put("start",
                    config.getProperty("start", "Stuttgart"));
            values.put("destination",
                    config.getProperty("destination", "Hamburg"));
            values.put("types",
                    config.getProperty("types", "1023"));
            values.put("difficulty",
                    config.getProperty("difficulty", "1-5"));
            values.put("terrain",
                    config.getProperty("terrain", "1-5"));

            values.put("emailHost",
                    config.getProperty("emailHost", "smtp.gmail.com"));
            values.put("emailPort",
                    config.getProperty("emailPort", "587"));
            values.put("emailSender",
                    config.getProperty("emailSender", "sender@gmail.com"));
            values.put("emailRecipient",
                    config.getProperty("emailRecipient", "receiver@gmail.com"));
            values.put("emailSubject",
                    config.getProperty("emailSubject", "oc_car - "
                            + Translation.getMessage("gpxForRoute")));
            values.put("emailBody",
                    config.getProperty("emailBody",
                            Translation.getMessage("gpxForRoute")));
            values.put("emailActive",
                    config.getProperty("emailActive", "false"));
            values.put("emailPassword",
                    config.getProperty("emailPassword", ""));
        } catch (IOException exception) {
            // pass
        }
    }

    /**
     * Write the values to the configuration file.
     */
    public static void writeConfig() {
        config.setProperty("ocUser",
                values.getOrDefault("ocUser", "User"));
        config.setProperty("radius",
                values.getOrDefault("radius", "2"));
        config.setProperty("start",
                values.getOrDefault("start", "Stuttgart"));
        config.setProperty("destination",
                values.getOrDefault("destination", "Hamburg"));
        config.setProperty("types",
                values.getOrDefault("types", "1023"));
        config.setProperty("difficulty",
                values.getOrDefault("difficulty", "1-5"));
        config.setProperty("terrain",
                values.getOrDefault("terrain", "1-5"));

        config.setProperty("emailHost",
                values.getOrDefault("emailHost", "smtp.gmail.com"));
        config.setProperty("emailPort",
                values.getOrDefault("emailPort", "587"));
        config.setProperty("emailSender",
                values.getOrDefault("emailSender", "sender@gmail.com"));
        config.setProperty("emailRecipient",
                values.getOrDefault("emailRecipient",
                        "receiver@gmail.com"));
        config.setProperty("emailSubject",
                values.getOrDefault("emailSubject", "oc_car - "
                        + Translation.getMessage("gpxForRoute")));
        config.setProperty("emailBody",
                values.getOrDefault("emailBody",
                        Translation.getMessage("gpxForRoute")));
        config.setProperty("emailActive",
                values.getOrDefault("emailActive", "false"));

        if (saveEmailPassword) {
            config.setProperty("emailPassword",
                    values.getOrDefault("emailPassword", ""));
        } else {
            config.setProperty("emailPassword", "");
        }

        File file = new File(FILEPATH);


        try (OutputStream outputStream = new FileOutputStream(file)) {
            config.storeToXML(outputStream,
                    "Configuration file for oc_car-gui");
        } catch (IOException exception) {
            // pass
        }
    }

    /**
     * Set the value for the given key.
     *
     * @param key The id.
     * @param value The value to set.
     */
    private static void setValue(String key, String value) {
        values.put(key, value);
    }

    /**
     * Get the value for the given key.
     *
     * @param key The id.
     * @return The value for the key or an empty string if the key does not
     *         exist.
     */
    private static String getValue(String key) {
        return values.getOrDefault(key, "");
    }

    /**
     * Set the username.
     *
     * @param user The name of the user to set.
     */
    public static void setUser(String user) {
        if (!user.isEmpty()) {
            setValue("ocUser", user);
        }
    }

    /**
     * Set the radius.
     *
     * @param radius The radius to set.
     */
    public static void setRadius(String radius) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        try {
            double radiusValue = Double.parseDouble(radius);
            if (radiusValue < 0.1) {
                JOptionPane.showMessageDialog(null,
                        Translation.getMessage("radiusTooSmall"));
                throw new NumberFormatException("Radius is too small.");
            }
            if (radiusValue > 10.0) {
                JOptionPane.showMessageDialog(null,
                        Translation.getMessage("radiusTooBig"));
                throw new NumberFormatException("Radius is too big.");
            }
            setValue("radius", decimalFormat.format(radiusValue));
        } catch (NumberFormatException exception) {
            // pass
        }
    }

    /**
     * Set the start.
     *
     * @param start The start to set.
     */
    public static void setStart(String start) {
        setValue("start", start);
    }

    /**
     * Set the destination.
     *
     * @param destination The destination to set.
     */
    public static void setDestination(String destination) {
        setValue("destination", destination);
    }

    /**
     * Set the cache types.
     *
     * @param types The types number as a string to set.
     */
    private static void setTypes(String types) {
        try {
            int typesValue = Integer.parseInt(types);

            if (typesValue < 1) {
                throw new NumberFormatException("No types chosen.");
            }
            if (typesValue > 1023) {
                throw new NumberFormatException("Too big type number.");
            }

            setValue("types", Integer.toString(typesValue));
        } catch (NumberFormatException exception) {
            // pass
        }
    }

    /**
     * Set the cache types.
     *
     * @param types The types number to set.
     */
    public static void setTypes(int types) {
        setTypes(Integer.toString(types));
    }

    /**
     * Format the number range (integer values). The output is something like
     * {@code 1-5} or {@code 1}.
     *
     * @param input The input string to format.
     * @return The formatted string.
     * @throws NumberFormatException The input is empty, contains invalid
     *                               integers or has more than two numbers.
     */
    private static String formatNumberRange(String input)
            throws NumberFormatException {
        String[] parts = input.split("-");

        if (parts.length > 2 || parts.length < 1) {
            throw new NumberFormatException("No number range given.");
        }

        for (int i = 0; i < parts.length; i++) {
            int temp = Integer.parseInt(parts[i]);
            parts[i] = Integer.toString(temp);
        }

        if (parts.length == 1) {
            return parts[0];
        }

        return (parts[0] + "-" + parts[1]);
    }

    /**
     * Set the difficulty range.
     *
     * @param difficulty The difficulty to set.
     */
    public static void setDifficulty(String difficulty) {
        try {
            setValue("difficulty", formatNumberRange(difficulty));
        } catch (NumberFormatException exception) {
            // pass
        }
    }

    /**
     * Set the terrain range.
     *
     * @param terrain The terrain to set.
     */
    public static void setTerrain(String terrain) {
        try {
            setValue("terrain", formatNumberRange(terrain));
        } catch (NumberFormatException exception) {
            // pass
        }
    }

    /**
     * Get the username.
     *
     * @return The saved username.
     */
    public static String getUser() {
        return getValue("ocUser");
    }

    /**
     * Get the radius.
     *
     * @return The saved radius.
     */
    public static String getRadius() {
        return getValue("radius");
    }

    /**
     * Get the start.
     *
     * @return The saved start.
     */
    public static String getStart() {
        return getValue("start");
    }

    /**
     * Get the destination.
     *
     * @return The saved destination.
     */
    public static String getDestination() {
        return getValue("destination");
    }

    /**
     * Get the cache types.
     *
     * @return The saved cache types number.
     */
    public static int getTypes() {
        return Integer.parseInt(getValue("types"));
    }

    /**
     * Get the difficulty range.
     *
     * @return The saved difficulty range.
     */
    public static String getDifficulty() {
        return getValue("difficulty");
    }

    /**
     * Get the terrain range.
     *
     * @return The saved terrain range.
     */
    public static String getTerrain() {
        return getValue("terrain");
    }

    /**
     * Set the email server host.
     *
     * @param host The host to set.
     */
    public static void setEmailHost(String host) {
        setValue("emailHost", host);
    }

    /**
     * Set the email server port.
     *
     * @param port The port to set.
     */
    public static void setEmailPort(String port) {
        try {
            int portValue = Integer.parseInt(port);

            if (portValue < 1) {
                throw new NumberFormatException("Invalid port.");
            }

            setValue("emailPort", Integer.toString(portValue));
        } catch (NumberFormatException exception) {
            // pass
        }
    }

    /**
     * Set the email sender.
     *
     * @param sender The sender to set.
     */
    public static void setEmailSender(String sender) {
        setValue("emailSender", sender);
    }

    /**
     * Set the email recipient.
     *
     * @param recipient The recipient to set.
     */
    public static void setEmailRecipient(String recipient) {
        setValue("emailRecipient", recipient);
    }

    /**
     * Set the email subject.
     *
     * @param subject The subject to set.
     */
    public static void setEmailSubject(String subject) {
        setValue("emailSubject", subject);
    }

    /**
     * Set the email body.
     *
     * @param body The body to set.
     */
    public static void setEmailBody(String body) {
        setValue("emailBody", body);
    }

    /**
     * Set the email password.
     *
     * @param password The password to set.
     */
    public static void setEmailPassword(char[] password) {
        setValue("emailPassword", new String(password));
    }

    /**
     * Set the status whether the email functionality is active.
     *
     * @param active Enable/disable email functionality.
     */
    public static void setEmailActive(boolean active) {
        setValue("emailActive", Boolean.toString(active));
    }

    /**
     * Set the status whether to save the email password.
     *
     * @param save Enable/disable password saving.
     */
    public static void setSaveEmailPassword(boolean save) {
        saveEmailPassword = save;
    }

    /**
     * Get the email server host.
     *
     * @return The saved host.
     */
    public static String getEmailHost() {
        return getValue("emailHost");
    }

    /**
     * Get the email server port.
     *
     * @return The saved port.
     */
    public static String getEmailPort() {
        return getValue("emailPort");
    }

    /**
     * Get the email sender.
     *
     * @return The saved sender.
     */
    public static String getEmailSender() {
        return getValue("emailSender");
    }

    /**
     * Get the email recipient.
     *
     * @return The saved recipient.
     */
    public static String getEmailRecipient() {
        return getValue("emailRecipient");
    }

    /**
     * Get the email subject.
     *
     * @return The saved subject.
     */
    public static String getEmailSubject() {
        return getValue("emailSubject");
    }

    /**
     * Get the email body.
     *
     * @return The saved body.
     */
    public static String getEmailBody() {
        return getValue("emailBody");
    }

    /**
     * Check if the email functionality is enabled.
     *
     * @return Whether the email functionality is enabled.
     */
    public static boolean isEmailActive() {
        return Boolean.parseBoolean(getValue("emailActive"));
    }

    /**
     * Get the email password.
     *
     * @return The saved password.
     */
    public static String getEmailPassword() {
        return getValue("emailPassword");
    }

    /**
     * Set the GPX file path.
     *
     * @param file The file path to set.
     */
    public static void setGpxFile(String file) {
        gpxLoadPath = file;
    }

    /**
     * Get the GPX file path.
     *
     * @return The saved file path.
     */
    public static String getGpxFile() {
        return gpxLoadPath;
    }
}
