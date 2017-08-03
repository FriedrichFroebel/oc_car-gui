package com.github.friedrichfroebel.occar;

import com.github.friedrichfroebel.occar.config.Configuration;
import com.github.friedrichfroebel.occar.config.Version;
import com.github.friedrichfroebel.occar.frame.CachetypeChooser;
import com.github.friedrichfroebel.occar.frame.GpxFileChooser;
import com.github.friedrichfroebel.occar.helper.Translation;
import com.github.friedrichfroebel.occar.network.GithubCom;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * This is the entry point for the user which shows the basic GUI.
 */
public class Gui extends JFrame {

    /**
     * The basic panel which holds all the content.
     */
    private static JPanel contentPane;

    /**
     * The input field for the username.
     */
    private static JTextField textUser;

    /**
     * The input field for the start.
     */
    private static JTextField textStart;

    /**
     * The input field for the destination.
     */
    private static JTextField textDestination;

    /**
     * The input field for the radius.
     */
    private static JTextField textRadius;

    /**
     * The input field for the difficulty.
     */
    private static JTextField textDifficultyRange;

    /**
     * The input field for the terrain.
     */
    private static JTextField textTerrainRange;

    /**
     * The input field for the email server host.
     */
    private static JTextField textEmailServer;

    /**
     * The input field for the email server port.
     */
    private static JTextField textEmailPort;

    /**
     * The input field for the email sender.
     */
    private static JTextField textEmailSender;

    /**
     * The input field for the email recipient.
     */
    private static JTextField textEmailRecipient;

    /**
     * The input field for the email subject.
     */
    private static JTextField textEmailSubject;

    /**
     * The input field for the email body.
     */
    private static JTextField textEmailBody;

    /**
     * The input field for the email password.
     */
    private static JPasswordField textEmailPassword;

    /**
     * The checkbox which determines whether to save the email password.
     */
    private static JCheckBox boxEmailSavePassword;

    /**
     * The checkbox which determines whether to send emails.
     */
    private static JCheckBox boxEmailActive;

    /**
     * The button which allows searching for all cache types.
     */
    private static JRadioButton buttonAllTypes;

    /**
     * The button which allows searching for specific cache types.
     */
    private static JRadioButton buttonSelectTypes;

    /**
     * The indicator which shows whether a GPX route has been loaded.
     */
    private static JLabel labelGpx;

    /**
     * The label which prints some (hopefully) helpful output at the end of the
     * run.
     */
    private static JLabel labelInfo;

    /**
     * Launch the application.
     *
     * @param args The commandline arguments. These are ignored at the moment.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Gui frame = new Gui();
                frame.setVisible(true);
            }
        });
    }

    /**
     * Create the frame.
     */
    private Gui() {
        Configuration.readConfig();

        setTitle(MessageFormat.format("Opencaching.de - {0} v{1}",
                Translation.getMessage("cachesAlongRoute"),
                Version.VERSION));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 390));
        setBounds(100, 100, 510, 390);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.5, 0.0};
        contentPane.setLayout(gridBagLayout);

        createSearchArea();
        createEmailArea();
        createInfoArea();
        addSeparators();

        setGuiText();

        checkForNewVersion();
    }

    /**
     * Create the search area.
     */
    private static void createSearchArea() {
        String[] labelIds = { "username", "start", "destination", "radiusKm",
            "types", "difficultyRange", "terrainRange" };
        for (int i = 0; i < labelIds.length; i++) {
            createFieldLabel(labelIds[i], 0, i);
        }

        textUser = new JTextField();
        textUser.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textUser.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textUser.setColumns(10);
        contentPane.add(textUser, createTextConstraints(1, 0, 2));

        textStart = new JTextField();
        textStart.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textStart.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textStart.setColumns(10);
        contentPane.add(textStart, createTextConstraints(1, 1, 2));

        textDestination = new JTextField();
        textDestination.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textDestination.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textDestination.setColumns(10);
        contentPane.add(textDestination, createTextConstraints(1, 2, 2));

        textRadius = new JTextField();
        textRadius.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textRadius.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textRadius.setColumns(10);
        contentPane.add(textRadius, createTextConstraints(1,3, 2));

        textDifficultyRange = new JTextField();
        textDifficultyRange.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textDifficultyRange.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textDifficultyRange.setColumns(10);
        contentPane.add(textDifficultyRange, createTextConstraints(1, 5, 2));

        textTerrainRange = new JTextField();
        textTerrainRange.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textTerrainRange.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textTerrainRange.setColumns(10);
        contentPane.add(textTerrainRange, createTextConstraints(1, 6, 2));

        addGpxLoader();
        addSearchButton();
        addTypeButtons();
    }

    /**
     * Create the button and label to load a GPX route.
     */
    private static void addGpxLoader() {
        JButton buttonLoadGpx = new JButton(Translation.getMessage("loadGpx"));

        buttonLoadGpx.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    GpxFileChooser.chooseGpxFile();
                    labelGpx.setVisible(!Configuration.getGpxFile().isEmpty());
                }
            }
        });
        buttonLoadGpx.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GpxFileChooser.chooseGpxFile();
                labelGpx.setVisible(!Configuration.getGpxFile().isEmpty());
            }
        });

        contentPane.add(buttonLoadGpx, createTextConstraints(3, 1, 1));

        labelGpx = new JLabel(Translation.getMessage("routeLoaded"));
        labelGpx.setHorizontalAlignment(SwingConstants.CENTER);
        labelGpx.setFont(new Font("Tahoma", Font.ITALIC, 11));
        labelGpx.setVisible(false);
        contentPane.add(labelGpx, createTextConstraints(3, 2, 1));
    }

    /**
     * Create the search button.
     */
    private static void addSearchButton() {
        JButton buttonSearch = new JButton(Translation.getMessage("search"));

        buttonSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateConfiguration();
                    labelInfo.setText(" ");
                    labelInfo.setText(Search.performSearch());
                }
            }
        });
        buttonSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateConfiguration();
                labelInfo.setText(" ");
                labelInfo.setText(Search.performSearch());
            }
        });

        contentPane.add(buttonSearch, createTextConstraints(3, 5, 1));
    }

    /**
     * Create the cache type buttons.
     */
    private static void addTypeButtons() {
        buttonAllTypes = new JRadioButton(Translation.getMessage("all"));

        buttonAllTypes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Configuration.setTypes(1023);
                updateConfiguration();
            }
        });

        contentPane.add(buttonAllTypes, createTextConstraints(1, 4, 1));

        buttonSelectTypes = new JRadioButton(Translation.getMessage("choose"));

        buttonSelectTypes.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Fix error that this button does not get enabled when clicked.
                if (Configuration.getTypes() == 1023) {
                    buttonAllTypes.setSelected(true);
                } else {
                    buttonSelectTypes.setSelected(true);
                }
            }
        });
        buttonSelectTypes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CachetypeChooser cachetypeChooser = new CachetypeChooser();
                cachetypeChooser.setVisible(true);
                updateConfiguration();
            }
        });

        contentPane.add(buttonSelectTypes, createTextConstraints(2, 4, 1));

        // Avoid that both buttons can be chosen at the same time.
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(buttonAllTypes);
        buttonGroup.add(buttonSelectTypes);
    }

    /**
     * Create the email area.
     */
    private void createEmailArea() {
        createEmailLabels();

        textEmailServer = new JTextField();
        textEmailServer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailServer.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textEmailServer.setColumns(10);
        contentPane.add(textEmailServer, createTextConstraints(1, 8, 1));

        textEmailPort = new JTextField();
        textEmailPort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailPort.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textEmailPort.setColumns(10);
        contentPane.add(textEmailPort, createTextConstraints(3, 8, 1));

        textEmailSender = new JTextField();
        textEmailSender.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailSender.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textEmailSender.setColumns(10);
        contentPane.add(textEmailSender, createTextConstraints(1, 9, 1));

        textEmailRecipient = new JTextField();
        textEmailRecipient.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailRecipient.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textEmailRecipient.setColumns(10);
        contentPane.add(textEmailRecipient, createTextConstraints(3, 9, 1));

        textEmailSubject = new JTextField();
        textEmailSubject.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailSubject.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textEmailSubject.setColumns(10);
        contentPane.add(textEmailSubject, createTextConstraints(1, 10, 3));

        textEmailBody = new JTextField();
        textEmailBody.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailBody.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textEmailBody.setColumns(10);
        contentPane.add(textEmailBody, createTextConstraints(1, 11, 3));

        textEmailPassword = new JPasswordField();
        textEmailPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textEmailPassword.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateConfiguration();
            }
        });
        textEmailPassword.setColumns(10);
        contentPane.add(textEmailPassword, createTextConstraints(1, 12, 1));

        addMailBoxes();
    }

    /**
     * Create the labels for the email input fields.
     */
    private static void createEmailLabels() {
        createFieldLabel("emailServer", 0, 8);
        createFieldLabel("emailPort", 2, 8);
        createFieldLabel("emailSender", 0, 9);
        createFieldLabel("emailRecipient", 2, 9);
        createFieldLabel("emailSubject", 0, 10);
        createFieldLabel("emailBody", 0, 11);
        createFieldLabel("emailPassword", 0, 12);
    }

    /**
     * Create the checkboxes for the email area.
     */
    private static void addMailBoxes() {
        boxEmailSavePassword = new JCheckBox(
                Translation.getMessage("emailSavePassword"));
        GridBagConstraints gridBagConstraintsSave = new GridBagConstraints();
        gridBagConstraintsSave.insets = new Insets(0,0, 5, 5);
        gridBagConstraintsSave.gridx = 2;
        gridBagConstraintsSave.gridy = 12;
        contentPane.add(boxEmailSavePassword, gridBagConstraintsSave);

        boxEmailActive = new JCheckBox(Translation.getMessage("emailSend"));
        GridBagConstraints gridBagConstraintsActive = new GridBagConstraints();
        gridBagConstraintsActive.insets = new Insets(0, 0, 5, 5);
        gridBagConstraintsActive.gridx = 3;
        gridBagConstraintsActive.gridy = 12;
        contentPane.add(boxEmailActive, gridBagConstraintsActive);
    }

    /**
     * Create the information area.
     */
    private static void createInfoArea() {
        labelInfo = new JLabel(" ");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        contentPane.add(labelInfo, gridBagConstraints);
    }

    /**
     * Create the separator lines between the different areas.
     */
    private static void addSeparators() {
        GridBagConstraints gridBagConstraintsUpper = new GridBagConstraints();
        gridBagConstraintsUpper.insets = new Insets(0,0,5,5);
        gridBagConstraintsUpper.gridwidth = 4;
        gridBagConstraintsUpper.gridx = 0;
        gridBagConstraintsUpper.gridy = 7;
        gridBagConstraintsUpper.fill = GridBagConstraints.HORIZONTAL;
        JSeparator upperSeparator = new JSeparator();
        contentPane.add(upperSeparator, gridBagConstraintsUpper);

        GridBagConstraints gridBagConstraintsLower = new GridBagConstraints();
        gridBagConstraintsLower.insets = new Insets(0,0,5,5);
        gridBagConstraintsLower.gridwidth = 4;
        gridBagConstraintsLower.gridx = 0;
        gridBagConstraintsLower.gridy = 13;
        gridBagConstraintsLower.fill = GridBagConstraints.HORIZONTAL;
        JSeparator lowerSeparator = new JSeparator();
        contentPane.add(lowerSeparator, gridBagConstraintsLower);
    }

    /**
     * Create the input field label. The text inside this label gets aligned
     * at the end of the line.
     *
     * @param name The message id for the label text.
     * @param offsetX The offset in x direction.
     * @param offsetY The offset in y direction.
     */
    private static void createFieldLabel(String name, int offsetX,
                                         int offsetY) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.gridx = offsetX;
        gridBagConstraints.gridy = offsetY;
        JLabel label = new JLabel(MessageFormat.format("{0}:",
                Translation.getMessage(name)));
        contentPane.add(label, gridBagConstraints);
    }

    /**
     * Create the constraints for the text input fields.
     *
     * @param offsetX The offset in x direction.
     * @param offsetY The offset in y direction.
     * @param width The number of columns to span.
     * @return The constraints build with the given values.
     */
    private static GridBagConstraints createTextConstraints(int offsetX,
                                                            int offsetY,
                                                            int width) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.gridwidth = width;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = offsetX;
        gridBagConstraints.gridy = offsetY;

        return gridBagConstraints;
    }

    /**
     * Update the configuration with the GUI values.
     */
    private static void updateConfiguration() {
        Configuration.setUser(textUser.getText());
        Configuration.setRadius(textRadius.getText());
        Configuration.setStart(textStart.getText());
        Configuration.setDestination(textDestination.getText());
        Configuration.setDifficulty(textDifficultyRange.getText());
        Configuration.setTerrain(textTerrainRange.getText());

        Configuration.setEmailHost(textEmailServer.getText());
        Configuration.setEmailPort(textEmailPort.getText());
        Configuration.setEmailSender(textEmailSender.getText());
        Configuration.setEmailRecipient(textEmailRecipient.getText());
        Configuration.setEmailSubject(textEmailSubject.getText());
        Configuration.setEmailBody(textEmailSubject.getText());
        Configuration.setEmailActive(boxEmailActive.isSelected());
        Configuration.setSaveEmailPassword(boxEmailSavePassword.isSelected());
        Configuration.setEmailPassword(textEmailPassword.getPassword());

        Configuration.writeConfig();

        setGuiText();
    }

    /**
     * Set the values of the input fields according to the configuration.
     */
    private static void setGuiText() {
        textUser.setText(Configuration.getUser());
        textRadius.setText(Configuration.getRadius());
        textStart.setText(Configuration.getStart());
        textDestination.setText(Configuration.getDestination());
        textDifficultyRange.setText(Configuration.getDifficulty());
        textTerrainRange.setText(Configuration.getTerrain());

        if (Configuration.getTypes() == 1023) {
            buttonAllTypes.setSelected(true);
        } else {
            buttonSelectTypes.setSelected(true);
        }

        textEmailServer.setText(Configuration.getEmailHost());
        textEmailPort.setText(Configuration.getEmailPort());
        textEmailSender.setText(Configuration.getEmailSender());
        textEmailRecipient.setText(Configuration.getEmailRecipient());
        textEmailSubject.setText(Configuration.getEmailSubject());
        textEmailBody.setText(Configuration.getEmailBody());
        boxEmailActive.setSelected(Configuration.isEmailActive());
    }

    /**
     * Check for a new application version.
     */
    private static void checkForNewVersion() {
        String response = GithubCom.getVersion();

        // Skip if there has been a connection error.
        if (response.length() == 0) {
            return;
        }

        if (!response.equals(Version.VERSION)) {
            JEditorPane editorPane = new JEditorPane("text/html",
                MessageFormat.format("<html><body>{0} (v{1}):"
                + "<br><a href=\"https://github.com/FriedrichFroebel/"
                + "oc_car-gui/releases/\">https://github.com/FriedrichFroebel/"
                + "oc_car-gui/releases/</a>"
                + "</body></html>", Translation.getMessage("newVersion"),
                        response.trim()));
            editorPane.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType().equals(HyperlinkEvent.EventType
                            .ACTIVATED) && Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(
                                new URI("https://github.com/Friedrich"
                                    + "Froebel/oc_car-gui/releases/"));
                        } catch (IOException | URISyntaxException exc) {
                            // pass
                        }
                    }
                }
            });
            editorPane.setEditable(false);
            editorPane.setBackground(new JLabel().getBackground());

            JOptionPane.showMessageDialog(null, editorPane,
                    Translation.getMessage("versionInfo"),
                    JOptionPane.INFORMATION_MESSAGE);

        }
    }
}
