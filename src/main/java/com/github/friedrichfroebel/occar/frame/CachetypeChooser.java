package com.github.friedrichfroebel.occar.frame;

import com.github.friedrichfroebel.occar.config.Configuration;
import com.github.friedrichfroebel.occar.helper.CachetypesBitmask;
import com.github.friedrichfroebel.occar.helper.Translation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * This is the window to choose the cache types.
 */
public class CachetypeChooser extends JFrame {

    /**
     * The panel which holds all the content.
     */
    private JPanel contentPane;

    /**
     * The constructor which creates the GUI elements.
     */
    public CachetypeChooser() {
        final String preselection =
                CachetypesBitmask.intToQuery(Configuration.getTypes());

        setTitle(Translation.getMessage("chooseTypes"));

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(320, 220));
        setBounds(175, 175, 320, 220);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        // Use GridBagLayout with three columns of equal weight.
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0};
        contentPane.setLayout(gridBagLayout);

        JCheckBox boxTradi = createCheckbox(
                Translation.getMessage("typeTradi"), 0, 0);
        boxTradi.setSelected(preselection.contains("Traditional"));

        JCheckBox boxMulti = createCheckbox(
                Translation.getMessage("typeMulti"), 0, 1);
        boxMulti.setSelected(preselection.contains("Multi"));

        JCheckBox boxMystery = createCheckbox(
                Translation.getMessage("typeMystery"), 0, 2);
        boxMystery.setSelected(preselection.contains("Quiz"));

        JCheckBox boxVirtual = createCheckbox(
                Translation.getMessage("typeVirtual"), 0, 3);
        boxVirtual.setSelected(preselection.contains("Virtual"));

        JCheckBox boxEvent = createCheckbox(
                Translation.getMessage("typeEvent"), 0, 4);
        boxEvent.setSelected(preselection.contains("Event"));

        JCheckBox boxWebcam = createCheckbox(
                Translation.getMessage("typeWebcam"), 1, 0);
        boxWebcam.setSelected(preselection.contains("Webcam"));

        JCheckBox boxMoving = createCheckbox(
                Translation.getMessage("typeMoving"), 1, 1);
        boxMoving.setSelected(preselection.contains("Moving"));

        JCheckBox boxMath = createCheckbox(
                Translation.getMessage("typeMath"), 1, 2);
        boxMath.setSelected(preselection.contains("Math/Physics"));

        JCheckBox boxDrive = createCheckbox(
                Translation.getMessage("typeDrive"), 1, 3);
        boxDrive.setSelected(preselection.contains("Drive-In"));

        JCheckBox boxOther = createCheckbox(
                Translation.getMessage("typeOther"), 1, 4);
        boxOther.setSelected(preselection.contains("Other"));

        JButton buttonApply = new JButton(
                Translation.getMessage("apply"));
        buttonApply.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Configuration.setTypes(CachetypesBitmask.booleanToInt(
                        boxTradi.isSelected(), boxMulti.isSelected(),
                        boxMystery.isSelected(), boxVirtual.isSelected(),
                        boxEvent.isSelected(), boxWebcam.isSelected(),
                        boxMoving.isSelected(), boxMath.isSelected(),
                        boxDrive.isSelected(), boxOther.isSelected()
                ));
                closeFrame();
            }
        });
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        contentPane.add(buttonApply, gridBagConstraints);
    }

    /**
     * Create a checkbox and add it to the content pane.
     *
     * @param name The label of the checkbox.
     * @param offsetX The offset in x direction.
     * @param offsetY The offset in y direction.
     * @return The requested checkbox.
     */
    private JCheckBox createCheckbox(String name, int offsetX,
                                            int offsetY) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.gridx = offsetX;
        gridBagConstraints.gridy = offsetY;
        JCheckBox checkBox = new JCheckBox(name);
        contentPane.add(checkBox, gridBagConstraints);
        return checkBox;
    }

    /**
     * Close the frame. This is only possible using an external function inside
     * the same class.
     */
    private void closeFrame() {
        super.dispose();
    }
}
