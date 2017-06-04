package com.github.friedrichfroebel.occar.helper;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is responsible for getting translations for the GUI labels.
 */
public final class Translation {

    /**
     * The resource where the messages are retrieved from.
     */
    private static ResourceBundle messages = null;

    /**
     * Create the bundle by checking whether the user uses German language
     * settings on the device. If he does not use German, the default language
     * is English.
     */
    private static void initBundle() {
        String language = Locale.getDefault().getLanguage();
        Locale locale = Locale.ENGLISH;

        if (language.startsWith("de")) {
            locale = Locale.GERMAN;
        }

        messages = ResourceBundle.getBundle("lang/messages", locale);
    }

    /**
     * Get the requested message.
     *
     * @param id The message id.
     * @return The requested message or an empty string if the message id is
     *         empty.
     */
    public static String getMessage(String id) {
        if (messages == null) {
            initBundle();
        }

        if (id != null && messages.containsKey(id)) {
            return messages.getString(id);
        }

        return "";
    }
}
