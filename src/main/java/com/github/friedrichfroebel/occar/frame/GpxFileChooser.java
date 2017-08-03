package com.github.friedrichfroebel.occar.frame;

import com.github.friedrichfroebel.occar.config.Configuration;
import com.github.friedrichfroebel.occar.helper.Translation;

import java.io.File;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

/**
 * This is the window which lets the user choose a GPX route file.
 */
public final class GpxFileChooser {

    /**
     * Open a file chooser dialog to select a GPX file.
     */
    public static void chooseGpxFile() {
        JFileChooser fileChooser = new JFileChooser();

        // Add a GPX file filter.
        FileNameExtensionFilter gpx = new FileNameExtensionFilter(
                Translation.getMessage("gpxFiles"), "gpx", "GPX");
        fileChooser.setFileFilter(gpx);

        // Set the start directory/file.
        File file;
        if (Configuration.getGpxFile().isEmpty()) {
            file = new File(System.getProperty("user.home") + File.separator
                    + "occar");
        } else {
            file = new File(Configuration.getGpxFile());
        }
        fileChooser.setCurrentDirectory(file);

        // Show the dialog itself.
        int option = fileChooser.showOpenDialog(null);

        if (option == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();

            // Check that the extension is GPX
            if (FilenameUtils.getExtension(path).toUpperCase().equals("GPX")) {
                Configuration.setGpxFile(path);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null,
                        MessageFormat.format("{0} '{1}'.",
                                Translation.getMessage("invalidExtensionGpx"),
                            FilenameUtils.getExtension(path) + "'."),
                        Translation.getMessage("invalidExtension"),
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
