package com.github.friedrichfroebel.occar.frame;

import com.github.friedrichfroebel.occar.helper.Translation;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

/**
 * This is the window which shows the progress of the requests.
 */
public class ProgressBar extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * The progress bar element.
     */
    private JProgressBar progressBar;  // NOPMD

    /**
     * Create the progress bar window.
     */
    public ProgressBar() {
        setResizable(false);  // NOPMD
        setTitle(Translation.getMessage("progress"));  // NOPMD
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // NOPMD
        setBounds(200, 200, 350, 80);  // NOPMD
        getContentPane().setLayout(null);  // NOPMD

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(10, 10, 330, 40);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        final Border border = BorderFactory.createTitledBorder(
                Translation.getMessage("performingQueries"));
        progressBar.setBorder(border);
        getContentPane().add(progressBar, BorderLayout.NORTH);  // NOPMD
    }

    /**
     * Update the bar.
     *
     * @param value Value to set the bar to.
     */
    public void updateBar(int value) {
        progressBar.setValue(value);

        // Force repainting of the window to see the status update.
        progressBar.paint(progressBar.getGraphics());
    }
}
