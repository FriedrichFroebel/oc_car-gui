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

    private static final String KEY_OC_USER = "ocUser";
    private static final String KEY_RADIUS = "radius";
    private static final String KEY_START = "start";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_TYPES = "types";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_TERRAIN = "terrain";
    private static final String KEY_EMAIL_HOST = "emailHost";
    private static final String KEY_EMAIL_PORT = "emailPort";
    private static final String KEY_EMAIL_SENDER = "emailSender";
    private static final String KEY_EMAIL_RECIPIENT = "emailRecipient";
    private static final String KEY_EMAIL_SUBJECT = "emailSubject";
    private static final String KEY_EMAIL_BODY = "emailBody";
    private static final String KEY_EMAIL_ACTIVE = "emailActive";
    private static final String KEY_EMAIL_PASSWORD = "emailPassword";

    private static final String DEFAULT_NUMBER_RANGE = "1-5";

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

            values.put(KEY_OC_USER,
                    config.getProperty(KEY_OC_USER, "User"));
            values.put(KEY_RADIUS,
                    config.getProperty(KEY_RADIUS, "2"));
            values.put(KEY_START,
                    config.getProperty(KEY_START, "Stuttgart"));
            values.put(KEY_DESTINATION,
                    config.getProperty(KEY_DESTINATION, "Hamburg"));
            values.put(KEY_TYPES,
                    config.getProperty(KEY_TYPES, "1023"));
            values.put(KEY_DIFFICULTY,
                    config.getProperty(KEY_DIFFICULTY, DEFAULT_NUMBER_RANGE));
            values.put(KEY_TERRAIN,
                    config.getProperty(KEY_TERRAIN, DEFAULT_NUMBER_RANGE));

            values.put(KEY_EMAIL_HOST,
                    config.getProperty(KEY_EMAIL_HOST, "smtp.gmail.com"));
            values.put(KEY_EMAIL_PORT,
                    config.getProperty(KEY_EMAIL_PORT, "587"));
            values.put(KEY_EMAIL_SENDER,
                    config.getProperty(KEY_EMAIL_SENDER, "sender@gmail.com"));
            values.put(
                KEY_EMAIL_RECIPIENT,
                config.getProperty(
                    KEY_EMAIL_RECIPIENT, "receiver@gmail.com"
                )
            );
            values.put(KEY_EMAIL_SUBJECT,
                    config.getProperty(KEY_EMAIL_SUBJECT, "oc_car - "
                            + Translation.getMessage("gpxForRoute")));  // NOPMD
            values.put(KEY_EMAIL_BODY,
                    config.getProperty(KEY_EMAIL_BODY,
                            Translation.getMessage("gpxForRoute")));  // NOPMD
            values.put(KEY_EMAIL_ACTIVE,
                    config.getProperty(KEY_EMAIL_ACTIVE, "false"));
            values.put(KEY_EMAIL_PASSWORD,
                    config.getProperty(KEY_EMAIL_PASSWORD, ""));
        } catch (IOException exception) {  // NOPMD
            // pass
        }
    }

    /**
     * Write the values to the configuration file.
     */
    public static void writeConfig() {
        config.setProperty(KEY_OC_USER,
                values.getOrDefault(KEY_OC_USER, "User"));
        config.setProperty(KEY_RADIUS,
                values.getOrDefault(KEY_RADIUS, "2"));
        config.setProperty(KEY_START,
                values.getOrDefault(KEY_START, "Stuttgart"));
        config.setProperty(KEY_DESTINATION,
                values.getOrDefault(KEY_DESTINATION, "Hamburg"));
        config.setProperty(KEY_TYPES,
                values.getOrDefault(KEY_TYPES, "1023"));
        config.setProperty(KEY_DIFFICULTY,
                values.getOrDefault(KEY_DIFFICULTY, DEFAULT_NUMBER_RANGE));
        config.setProperty(KEY_TERRAIN,
                values.getOrDefault(KEY_TERRAIN, DEFAULT_NUMBER_RANGE));

        config.setProperty(KEY_EMAIL_HOST,
                values.getOrDefault(KEY_EMAIL_HOST, "smtp.gmail.com"));
        config.setProperty(KEY_EMAIL_PORT,
                values.getOrDefault(KEY_EMAIL_PORT, "587"));
        config.setProperty(KEY_EMAIL_SENDER,
                values.getOrDefault(KEY_EMAIL_SENDER, "sender@gmail.com"));
        config.setProperty(KEY_EMAIL_RECIPIENT,
                values.getOrDefault(KEY_EMAIL_RECIPIENT,
                        "receiver@gmail.com"));
        config.setProperty(KEY_EMAIL_SUBJECT,
                values.getOrDefault(KEY_EMAIL_SUBJECT, "oc_car - "
                        + Translation.getMessage("gpxForRoute")));  // NOPMD
        config.setProperty(KEY_EMAIL_BODY,
                values.getOrDefault(KEY_EMAIL_BODY,
                        Translation.getMessage("gpxForRoute")));  // NOPMD
        config.setProperty(KEY_EMAIL_ACTIVE,
                values.getOrDefault(KEY_EMAIL_ACTIVE, "false"));

        if (saveEmailPassword) {
            config.setProperty(KEY_EMAIL_PASSWORD,
                    values.getOrDefault(KEY_EMAIL_PASSWORD, ""));
        } else {
            config.setProperty(KEY_EMAIL_PASSWORD, "");
        }

        File file = new File(FILEPATH);

        // Avoid errors when path or file does not exist.
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException exception) {
                return;
            }
        }

        try (OutputStream outputStream = new FileOutputStream(file)) {
            config.storeToXML(outputStream,
                    "Configuration file for oc_car-gui");
        } catch (IOException | SecurityException exception) {  // NOPMD
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
            setValue(KEY_OC_USER, user);
        }
    }

    /**
     * Set the radius.
     *
     * @param radius The radius to set.
     */
    public static void setRadius(String radius) {
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        try {
            final float radiusValue = Float.parseFloat(radius);
            if (radiusValue < 0.1f) {  // NOPMD
                JOptionPane.showMessageDialog(null,
                        Translation.getMessage("radiusTooSmall"));
                throw new NumberFormatException(
                        Translation.getMessage("radiusTooSmall"));
            }
            if (radiusValue > 10.0f) {  // NOPMD
                JOptionPane.showMessageDialog(null,
                        Translation.getMessage("radiusTooBig"));
                throw new NumberFormatException(
                        Translation.getMessage("radiusTooBig"));
            }
            setValue(KEY_RADIUS, decimalFormat.format(radiusValue));
        } catch (NumberFormatException exception) {  // NOPMD
            // pass
        }
    }

    /**
     * Set the start.
     *
     * @param start The start to set.
     */
    public static void setStart(String start) {
        setValue(KEY_START, start);
    }

    /**
     * Set the destination.
     *
     * @param destination The destination to set.
     */
    public static void setDestination(String destination) {
        setValue(KEY_DESTINATION, destination);
    }

    /**
     * Set the cache types.
     *
     * @param types The types number as a string to set.
     */
    private static void setTypes(String types) {
        try {
            final int typesValue = Integer.parseInt(types);

            if (typesValue < 1) {  // NOPMD
                throw new NumberFormatException(
                        Translation.getMessage("noTypesChosen"));
            }
            if (typesValue > 1023) {  // NOPMD
                throw new NumberFormatException(
                        Translation.getMessage("typeNumberTooBig"));
            }

            setValue(KEY_TYPES, Integer.toString(typesValue));
        } catch (NumberFormatException exception) {  // NOPMD
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
        final String[] parts = input.split("-");
        final int partsCount = parts.length;

        if (partsCount > 2 || partsCount < 1) {
            throw new NumberFormatException(
                    Translation.getMessage("noNumberRange"));
        }

        for (int i = 0; i < partsCount; i++) {
            final int temp = Integer.parseInt(parts[i]);
            parts[i] = Integer.toString(temp);
        }

        if (partsCount == 1) {  // NOPMD
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
            setValue(KEY_DIFFICULTY, formatNumberRange(difficulty));
        } catch (NumberFormatException exception) {  // NOPMD
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
        } catch (NumberFormatException exception) {  // NOPMD
            // pass
        }
    }

    /**
     * Get the username.
     *
     * @return The saved username.
     */
    public static String getUser() {
        return getValue(KEY_OC_USER);
    }

    /**
     * Get the radius.
     *
     * @return The saved radius.
     */
    public static String getRadius() {
        return getValue(KEY_RADIUS);
    }

    /**
     * Get the start.
     *
     * @return The saved start.
     */
    public static String getStart() {
        return getValue(KEY_START);
    }

    /**
     * Get the destination.
     *
     * @return The saved destination.
     */
    public static String getDestination() {
        return getValue(KEY_DESTINATION);
    }

    /**
     * Get the cache types.
     *
     * @return The saved cache types number.
     */
    public static int getTypes() {
        int value;
        try {
            value = Integer.parseInt(getValue(KEY_TYPES));
        } catch (NumberFormatException exception) {
            value = 1023;
        }
        return value;
    }

    /**
     * Get the difficulty range.
     *
     * @return The saved difficulty range.
     */
    public static String getDifficulty() {
        return getValue(KEY_DIFFICULTY);
    }

    /**
     * Get the terrain range.
     *
     * @return The saved terrain range.
     */
    public static String getTerrain() {
        return getValue(KEY_TERRAIN);
    }

    /**
     * Set the email server host.
     *
     * @param host The host to set.
     */
    public static void setEmailHost(String host) {
        setValue(KEY_EMAIL_HOST, host);
    }

    /**
     * Set the email server port.
     *
     * @param port The port to set.
     */
    public static void setEmailPort(String port) {
        try {
            final int portValue = Integer.parseInt(port);

            if (portValue < 1) {  // NOPMD
                throw new NumberFormatException("Invalid port.");
            }

            setValue(KEY_EMAIL_PORT, Integer.toString(portValue));
        } catch (NumberFormatException exception) {  // NOPMD
            // pass
        }
    }

    /**
     * Set the email sender.
     *
     * @param sender The sender to set.
     */
    public static void setEmailSender(String sender) {
        setValue(KEY_EMAIL_SENDER, sender);
    }

    /**
     * Set the email recipient.
     *
     * @param recipient The recipient to set.
     */
    public static void setEmailRecipient(String recipient) {
        setValue(KEY_EMAIL_RECIPIENT, recipient);
    }

    /**
     * Set the email subject.
     *
     * @param subject The subject to set.
     */
    public static void setEmailSubject(String subject) {
        setValue(KEY_EMAIL_SUBJECT, subject);
    }

    /**
     * Set the email body.
     *
     * @param body The body to set.
     */
    public static void setEmailBody(String body) {
        setValue(KEY_EMAIL_BODY, body);
    }

    /**
     * Set the email password.
     *
     * @param password The password to set.
     */
    public static void setEmailPassword(char[] password) {
        setValue(KEY_EMAIL_PASSWORD, new String(password));
    }

    /**
     * Set the status whether the email functionality is active.
     *
     * @param active Enable/disable email functionality.
     */
    public static void setEmailActive(boolean active) {
        setValue(KEY_EMAIL_ACTIVE, Boolean.toString(active));
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
        return getValue(KEY_EMAIL_HOST);
    }

    /**
     * Get the email server port.
     *
     * @return The saved port.
     */
    public static String getEmailPort() {
        return getValue(KEY_EMAIL_PORT);
    }

    /**
     * Get the email sender.
     *
     * @return The saved sender.
     */
    public static String getEmailSender() {
        return getValue(KEY_EMAIL_SENDER);
    }

    /**
     * Get the email recipient.
     *
     * @return The saved recipient.
     */
    public static String getEmailRecipient() {
        return getValue(KEY_EMAIL_RECIPIENT);
    }

    /**
     * Get the email subject.
     *
     * @return The saved subject.
     */
    public static String getEmailSubject() {
        return getValue(KEY_EMAIL_SUBJECT);
    }

    /**
     * Get the email body.
     *
     * @return The saved body.
     */
    public static String getEmailBody() {
        return getValue(KEY_EMAIL_BODY);
    }

    /**
     * Check if the email functionality is enabled.
     *
     * @return Whether the email functionality is enabled.
     */
    public static boolean isEmailActive() {
        return Boolean.parseBoolean(getValue(KEY_EMAIL_ACTIVE));
    }

    /**
     * Get the email password.
     *
     * @return The saved password.
     */
    public static String getEmailPassword() {
        return getValue(KEY_EMAIL_PASSWORD);
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
     * Get the GPX file p´ath.
     *
     * @return The saved file path.
     */
    public static String getGpxFile() {
        return gpxLoadPath;
    }
}
