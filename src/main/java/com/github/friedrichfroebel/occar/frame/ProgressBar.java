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

    /**
     * The progress bar element.
     */
    private JProgressBar progressBar;

    /**
     * Create the progress bar window.
     */
    public ProgressBar() {
        setResizable(false);
        setTitle(Translation.getMessage("progress"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(200, 200, 350, 80);
        getContentPane().setLayout(null);

        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(10, 10, 330, 40);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        final Border border = BorderFactory.createTitledBorder(
                Translation.getMessage("performingQueries"));
        progressBar.setBorder(border);
        getContentPane().add(progressBar, BorderLayout.NORTH);
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
