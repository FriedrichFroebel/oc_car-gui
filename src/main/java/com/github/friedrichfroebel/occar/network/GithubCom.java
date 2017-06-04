package com.github.friedrichfroebel.occar.network;

import java.io.IOException;

/**
 * This class handles the communication with Github.com.
 */
public class GithubCom {

    /**
     * Get the version from the application repository.
     *
     * @return The retrieved version.
     */
    public static String getVersion() {
        String data;
        try {
            data = RequestBase.getPageContent(
                    "https://raw.githubusercontent.com/FriedrichFroebel/"
                    + "oc_car-gui/master/version");
        } catch (IOException exception) {
            return "";
        }

        return data;
    }
}
