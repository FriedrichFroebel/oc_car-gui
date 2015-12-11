import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.EventQueue;
import java.awt.Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;

import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;

public class oc_car extends JFrame {
	
	//Programmversion zur Analyse für eventuell verfügbares Update
	private static String version = "1.0";
	
	//erweiterte Konsolenausgabe ist standardmäßig deaktiviert
	private static boolean debug = false;
	
	//Variablen, die von anderen Fenstern beeinflusst werden
	private static String loadpath = "";
	private static boolean alleArten = true;
	private static boolean loadGPX = false;
	
	//Konfigurationswerte
	private static String ocUser = "User";
	private static double Radius = 2;
	private static String Start = "Stuttgart";
	private static String Ziel = "München";
	private static int Arten = 1023;
	private static String Difficulty = "1-5";
	private static String Terrain = "1-5";
	
	//Einbinden einer Datei mit Konfigurationswerten ermöglichen
	private Properties config = new Properties();
	
	//Radius als String formatieren
	private static DecimalFormat df_radius = new DecimalFormat( "#.##");
	
	//programminterne Daten
	private static String UUID = "";
	private static String lngS = "";
	private static String latS = "";
	private static String lngZ = "";
	private static String latZ = "";
	
	//Koordinaten der Route
	private static String[] coords_list = new String[10000]; 
	private static int anzahlAbfragen = 0;
	
	//gefundene Caches
	private static String[] caches = new String[10000];
	private static int ohneDuplikate = 0;
	
	//E-Mail-Einstellungen
	private sendEmail email = new sendEmail();
	private static String host = "smtp.gmail.com";
	private static String port = "587";
	private static String sender = "absender@gmail.com";
	private static String receiver = "empfaenger@gmail.com";
	private static String password = "";
	private static String subject = "oc_car - Die GPX-Datei für Deine Route";
	private static String body = "Die GPX-Datei für Deine Route!";

	//GUI-Elemente, auf die zugegriffen werden muss
	private JPanel contentPane;
	private JTextField tfBenutzer;
	private JTextField tfStart;
	private JTextField tfZiel;
	private JTextField tfRadius;
	private JTextField tfSchwierigkeitsbereich;
	private JTextField tfTerrainbereich;
	private JTextField tfMailserver;
	private JTextField tfAbsender;
	private JTextField tfEmpfaenger;
	private JTextField tfBetreff;
	private JTextField tfMailtext;
	private JRadioButton rdtbnAlle;
	private JRadioButton rdtbnAuswaehlen;
	private JTextField tfPort;
	private JLabel lblFortschritt = new JLabel("");;

	//Anwendung starten
	public static void main(String[] args) {
		try {
			if (!args[0].equals(" ")) {
				debug = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			//do nothing
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					oc_car mainframe = new oc_car();
					mainframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Frame erstellen
	public oc_car() {
		setTitle("Opencaching.de - Caches entlang einer Route"); //Titel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Aktion beim Drücken des X
		setBounds(100, 100, 500, 393); //Position und Abmessungen
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Beschriftungen bei Textfeldern für Routenabfrage
		
		JLabel lblBenutzer = new JLabel("Benutzer:");
		lblBenutzer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBenutzer.setBounds(10, 10, 136, 15);
		contentPane.add(lblBenutzer);
		
		JLabel lblStart = new JLabel("Start:");
		lblStart.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStart.setBounds(10, 32, 136, 15);
		contentPane.add(lblStart);
		
		JLabel lblZiel = new JLabel("Ziel:");
		lblZiel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblZiel.setBounds(10, 54, 136, 15);
		contentPane.add(lblZiel);
		
		JLabel lblRadius = new JLabel("Radius (in km):");
		lblRadius.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRadius.setBounds(10, 76, 136, 15);
		contentPane.add(lblRadius);
		
		JLabel lblCachearten = new JLabel("Cachearten:");
		lblCachearten.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCachearten.setBounds(10, 98, 136, 15);
		contentPane.add(lblCachearten);
		
		JLabel lblSchwierigkeitsbereich = new JLabel("Schwierigkeitsbereich:");
		lblSchwierigkeitsbereich.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSchwierigkeitsbereich.setBounds(10, 123, 136, 15);
		contentPane.add(lblSchwierigkeitsbereich);
		
		JLabel lblTerrainbereich = new JLabel("Terrainbereich:");
		lblTerrainbereich.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTerrainbereich.setBounds(10, 145, 136, 15);
		contentPane.add(lblTerrainbereich);
		
		//Textfelder für Routenabfrage
		
		tfBenutzer = new JTextField();
		tfBenutzer.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				tfBenutzer.selectAll();
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				ocUser = tfBenutzer.getText();
				writeConfig();
			}
		});
		tfBenutzer.setBounds(152, 8, 150, 20);
		contentPane.add(tfBenutzer);
		tfBenutzer.setColumns(10);
		
		tfStart = new JTextField();
		tfStart.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfStart.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				Start = tfStart.getText();
				writeConfig();
			}
		});
		tfStart.setColumns(10);
		tfStart.setBounds(152, 30, 150, 20);
		contentPane.add(tfStart);
		
		tfZiel = new JTextField();
		tfZiel.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfZiel.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				Ziel = tfZiel.getText();
				writeConfig();
			}
		});
		tfZiel.setColumns(10);
		tfZiel.setBounds(152, 52, 150, 20);
		contentPane.add(tfZiel);
		
		tfRadius = new JTextField();
		tfRadius.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfRadius.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				try {
					Radius = Double.parseDouble(tfRadius.getText());
				} catch (NumberFormatException ex) {
					tfRadius.setText(df_radius.format(Radius));
					if (debug) ex.printStackTrace();
				}
				writeConfig();
			}
		});
		tfRadius.setColumns(10);
		tfRadius.setBounds(152, 74, 100, 20);
		contentPane.add(tfRadius);
		
		tfSchwierigkeitsbereich = new JTextField();
		tfSchwierigkeitsbereich.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfSchwierigkeitsbereich.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				Difficulty = tfSchwierigkeitsbereich.getText();
				writeConfig();
			}
		});
		tfSchwierigkeitsbereich.setColumns(10);
		tfSchwierigkeitsbereich.setBounds(152, 121, 100, 20);
		contentPane.add(tfSchwierigkeitsbereich);
		
		tfTerrainbereich = new JTextField();
		tfTerrainbereich.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfTerrainbereich.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				Terrain = tfTerrainbereich.getText();
				writeConfig();
			}
		});
		tfTerrainbereich.setColumns(10);
		tfTerrainbereich.setBounds(152, 143, 100, 20);
		contentPane.add(tfTerrainbereich);
		
		//Radiobuttons für Auswahl der Cachearten
		
		JRadioButton rdbtnAlle = new JRadioButton("alle");
		rdbtnAlle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				alleArten = true;
			}
		});
		rdbtnAlle.setBounds(152, 96, 50, 23);
		contentPane.add(rdbtnAlle);
		
		JRadioButton rdbtnAuswaehlen = new JRadioButton("ausw\u00E4hlen");
		rdbtnAuswaehlen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					arten_choose artenwahl = new arten_choose();
					artenwahl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				writeConfig();
				alleArten = false;
			}
		});
		rdbtnAuswaehlen.setBounds(204, 96, 100, 23);
		contentPane.add(rdbtnAuswaehlen);
		
		//Radiobuttons gruppieren, damit nur einer ausgewählt werden kann
		ButtonGroup arten = new ButtonGroup();
		arten.add(rdbtnAlle);
		arten.add(rdbtnAuswaehlen);
		
		//Beschriftung, wenn GPX-Route geladen
		JLabel lblGpxGeladen = new JLabel("Route geladen");
		lblGpxGeladen.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblGpxGeladen.setHorizontalAlignment(SwingConstants.CENTER);
		lblGpxGeladen.setBounds(320, 54, 130, 14);
		contentPane.add(lblGpxGeladen);
		lblGpxGeladen.setVisible(false);
		
		//GPX-Route laden via JFileChooser
		JButton btnGpxRouteLaden = new JButton("GPX-Route laden");
		btnGpxRouteLaden.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) { //Enter-Taste gedrückt
					JFileChooser gpxeingabe = new JFileChooser();
					FileNameExtensionFilter gpx = new FileNameExtensionFilter("GPX-Dateien", "gpx", "GPX");
					gpxeingabe.setFileFilter(gpx);
					if (loadpath.equals("")) {
						File f = new File(System.getProperty("user.home") + File.separator + "occar");
						gpxeingabe.setCurrentDirectory(f);
					} else {
						File f = new File(loadpath);
						gpxeingabe.setCurrentDirectory(f);
					}
					int option = gpxeingabe.showOpenDialog(null);
					if (option == JFileChooser.APPROVE_OPTION) {
						String pfadEingabe = gpxeingabe.getSelectedFile().getAbsolutePath();
						if (FilenameUtils.getExtension(pfadEingabe).toUpperCase().equals("GPX")) {
							//System.out.println(pfadEingabe);
							loadpath = pfadEingabe;
							loadGPX = true;
							lblGpxGeladen.setVisible(true);
						} else {
							javax.swing.JOptionPane.showMessageDialog(null,
									"Dateiendung ist nicht GPX, sondern \""
									+ FilenameUtils.getExtension(pfadEingabe) + "\".",
									"Fehlerhafte Dateiendung",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		});
		btnGpxRouteLaden.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser gpxeingabe = new JFileChooser(); //Dateibrowser
				//nur GPX-Dateien erlauben
				FileNameExtensionFilter gpx = new FileNameExtensionFilter("GPX-Dateien", "gpx", "GPX");
				gpxeingabe.setFileFilter(gpx);
				if (loadpath.equals("")) { //bisher noch keine (gültige) GPX-Datei geladen
					File f = new File(System.getProperty("user.home") + File.separator + "occar");
					gpxeingabe.setCurrentDirectory(f);
				} else { //bereits einmal GPX-Datei geladen
					File f = new File(loadpath);
					gpxeingabe.setCurrentDirectory(f);
				}
				int option = gpxeingabe.showOpenDialog(null);
				//wenn Datei ausgewählt worden ist
				if (option == JFileChooser.APPROVE_OPTION) {
					//Pfad ermitteln
					String pfadEingabe = gpxeingabe.getSelectedFile().getAbsolutePath();
					//Dateiendung prüfen
					if (FilenameUtils.getExtension(pfadEingabe).toUpperCase().equals("GPX")) {
						if (debug) System.out.println(pfadEingabe);
						loadpath = pfadEingabe;
						loadGPX = true;
						lblGpxGeladen.setVisible(true); //Info anzeigen
					} else {
						javax.swing.JOptionPane.showMessageDialog(null,
								"Dateiendung ist nicht GPX, sondern \""
								+ FilenameUtils.getExtension(pfadEingabe) + "\".",
								"Fehlerhafte Dateiendung",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		btnGpxRouteLaden.setBounds(320, 28, 130, 23);
		contentPane.add(btnGpxRouteLaden);
		
		//Trennstrich zwischen Routenkonfiguration und E-Mail-Einstellungen
		JSeparator sepAllgemeinMail = new JSeparator();
		sepAllgemeinMail.setBounds(10, 170, 460, 2);
		contentPane.add(sepAllgemeinMail);
		
		//Beschriftungen bei Textfeldern für E-Mail-Versand
		
		JLabel lblMailserver = new JLabel("E-Mail-Server:");
		lblMailserver.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMailserver.setBounds(10, 181, 100, 14);
		contentPane.add(lblMailserver);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPort.setBounds(235, 181, 100, 14);
		contentPane.add(lblPort);
		
		JLabel lblAbsender = new JLabel("Absender:");
		lblAbsender.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAbsender.setBounds(10, 209, 100, 14);
		contentPane.add(lblAbsender);
		
		JLabel lblEmpfaenger = new JLabel("Empf\u00E4nger:");
		lblEmpfaenger.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmpfaenger.setBounds(235, 209, 100, 14);
		contentPane.add(lblEmpfaenger);
		
		JLabel lblBetreff = new JLabel("Betreff:");
		lblBetreff.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBetreff.setBounds(10, 237, 100, 14);
		contentPane.add(lblBetreff);
		
		JLabel lblMailtext = new JLabel("E-Mail-Text:");
		lblMailtext.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMailtext.setBounds(10, 265, 100, 14);
		contentPane.add(lblMailtext);
		
		//Textfelder für E-Mail-Versand
		
		tfMailserver = new JTextField();
		tfMailserver.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfMailserver.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				host = tfMailserver.getText();
				writeConfig();
			}
		});
		tfMailserver.setBounds(113, 179, 120, 20);
		contentPane.add(tfMailserver);
		tfMailserver.setColumns(10);
		
		tfAbsender = new JTextField();
		tfAbsender.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfAbsender.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				sender = tfAbsender.getText();
				writeConfig();
			}
		});
		tfAbsender.setBounds(113, 207, 120, 20);
		contentPane.add(tfAbsender);
		tfAbsender.setColumns(10);
		
		tfEmpfaenger = new JTextField();
		tfEmpfaenger.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfEmpfaenger.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				receiver = tfEmpfaenger.getText();
				writeConfig();
			}
		});
		tfEmpfaenger.setColumns(10);
		tfEmpfaenger.setBounds(338, 207, 120, 20);
		contentPane.add(tfEmpfaenger);
		
		tfBetreff = new JTextField();
		tfBetreff.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfBetreff.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				subject = tfBetreff.getText();
				writeConfig();
			}
		});
		tfBetreff.setBounds(113, 235, 345, 20);
		contentPane.add(tfBetreff);
		tfBetreff.setColumns(10);
		
		tfMailtext = new JTextField();
		tfMailtext.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfMailtext.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				body = tfMailtext.getText();
				writeConfig();
			}
		});
		tfMailtext.setBounds(113, 263, 345, 20);
		contentPane.add(tfMailtext);
		tfMailtext.setColumns(10);
		
		tfPort = new JTextField();
		tfPort.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				tfPort.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				try {
					port = Integer.toString(Integer.parseInt(tfPort.getText()));
				} catch (NumberFormatException ex) {
					tfPort.setText(port);
					if (debug) ex.printStackTrace();
				}
				writeConfig();
			}
		});
		tfPort.setBounds(338, 179, 120, 20);
		contentPane.add(tfPort);
		tfPort.setColumns(10);
		
		JLabel lblPasswort = new JLabel("Passwort:");
		lblPasswort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPasswort.setBounds(10, 293, 100, 14);
		contentPane.add(lblPasswort);
		
		JPasswordField pfPassword = new JPasswordField();
		pfPassword.setBounds(113, 291, 120, 20);
		contentPane.add(pfPassword);
		
		//Trennstrich zwischen E-Mail-Einstellungen und Meldungslabel
		JSeparator sepMailProgress = new JSeparator();
		sepMailProgress.setBounds(10, 318, 460, 2);
		contentPane.add(sepMailProgress);
		
		//Meldungslabel	
		lblFortschritt.setHorizontalAlignment(SwingConstants.CENTER);
		lblFortschritt.setBounds(10, 327, 460, 20);
		contentPane.add(lblFortschritt);
		
		//Button zum Starten der Suche
		JButton btnStartSuche = new JButton("Caches suchen");
		btnStartSuche.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (rdtbnAlle.isSelected()) {
						alleArten = true;
					} else {
						alleArten = false;
					}
					try {
						char[] input = pfPassword.getPassword();
						String temp = "";
						for (int i = 0; i < pfPassword.getPassword().length; i++) {
							temp += input[i];
						}
						password = temp;
					} catch (ArrayIndexOutOfBoundsException ex) {
						if (debug) System.out.println("Kein Passwort angegeben");
					}
					sucheStarten();
					lblGpxGeladen.setVisible(false);
					loadGPX = false;
				}
			}
		});
		btnStartSuche.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Passwort ermitteln
				try {
					char[] input = pfPassword.getPassword();
					String temp = "";
					for (int i = 0; i < pfPassword.getPassword().length; i++) {
						temp += input[i];
					}
					password = temp;
				} catch (ArrayIndexOutOfBoundsException e) {
					if (debug) System.out.println("Kein Passwort angegeben");
				}
				sucheStarten(); //Suche durch Funktionsaufruf starten
				lblGpxGeladen.setVisible(false); //eventuelll sichtbares Label deaktivieren
				loadGPX = false; //keine GPX-Datei mehr geladen
			}
		});
		btnStartSuche.setBounds(320, 119, 130, 23);
		contentPane.add(btnStartSuche);
		
		checkNewVersion(); //auf neue Version prüfen
		readConfig(); //Konfigurationsdatei lesen
		printInitialConfig(); //Konfiguration ausgeben
		
		//einen Radiobutton entsprechend der Konfiguration vorbelegen
		if (Arten == 1023) {
			rdbtnAlle.setSelected(true);
			alleArten = true;
		} else {
			rdtbnAuswaehlen.setSelected(true);
			alleArten = false;
		}
		
	}

	//Zahlenwert für Cachearten erhalten (in Klasse arten_choose verwendet)
	public int getCachearten() {
		return Arten;
	}

	//Zahlenwert für Cachearten setzen (in Klasse arten_choose verwendet)
	public void setCachearten(int cachearten) {
		oc_car.Arten = cachearten;
	}
	
	//Parameter in Konfigurationsdatei schreiben
	private boolean writeConfig() {
		try {
			//Parameter für Routenabfrage
			config.setProperty("ocUser", ocUser);
			config.setProperty("Radius", df_radius.format(Radius));
			config.setProperty("Start", Start);
			config.setProperty("Ziel", Ziel);
			config.setProperty("Arten", Integer.toString(Arten));
			config.setProperty("Difficulty", Difficulty);
			config.setProperty("Terrain", Terrain);
			
			//Parameter für E-Mail-Versand
			config.setProperty("host", host);
			config.setProperty("port", port);
			config.setProperty("sender", sender);
			config.setProperty("receiver", receiver);
			config.setProperty("subject", subject);
			config.setProperty("body", body);
			
			//in XML-Datei schreiben
			File f = new File(System.getProperty("user.home") + File.separator + "occar" + File.separator + "occar_config.xml");
			OutputStream o = new FileOutputStream(f);
			config.storeToXML(o, "Config-Datei für oc_car");
		} catch (IOException e) {
			if (debug) e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//Parameter aus Konfiguration lesen
	private boolean readConfig() {
		InputStream s = null;
		try {
			//Dateipfad zusammensetzen
			File f = new File(System.getProperty("user.home") + File.separator + "occar" + File.separator + "occar_config.xml");
			if (!f.exists()) { //wenn Datei nicht existiert
				writeConfig(); //Datei mit Inhalt erstellen
			} else { //Datei existiert
				//aus XML-Datei lesen
				s = new FileInputStream(f);
				config.loadFromXML(s);
				
				//Parameter für Routenabfrage
				ocUser = config.getProperty("ocUser", "User");
				Radius = Double.parseDouble(config.getProperty("Radius", "2"));
				Start = config.getProperty("Start", "Stuttgart");
				Ziel = config.getProperty("Ziel", "München");
				Arten = Integer.parseInt(config.getProperty("Arten", "1023"));
				Difficulty = config.getProperty("Difficulty", "1-5");
				Terrain = config.getProperty("Terrain", "1-5");
				
				//Parameter für E-Mail-Versand
				host = config.getProperty("host", "smtp.gmail.com");
				port = config.getProperty("port", "587");
				sender = config.getProperty("sender", "absender@gmail.com");
				receiver = config.getProperty("receiver", "empfaenger@gmail.com");
				subject = config.getProperty("subject", "oc_car - Die GPX-Datei für Deine Route");
				body = config.getProperty("body", "Die GPX-Datei für Deine Route!");
			}
		} catch (IOException e) {
			if (debug) e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//am Programmbeginn Daten aus Konfigurationsdatei in die Textfelder schreiben
	private void printInitialConfig() {
		tfBenutzer.setText(ocUser);
		tfStart.setText(Start);
		tfZiel.setText(Ziel);
		tfRadius.setText(df_radius.format(Radius));
		tfSchwierigkeitsbereich.setText(Difficulty);
		tfTerrainbereich.setText(Terrain);
		
		tfMailserver.setText(host);
		tfAbsender.setText(sender);
		tfEmpfaenger.setText(receiver);
		tfBetreff.setText(subject);
		tfMailtext.setText(body);
		tfPort.setText(port);
	}
	
	//Opencaching.de Benutzer-ID ermitteln
	private boolean getUUID() {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line = "";

	    try {
	        url = new URL("http://www.opencaching.de/okapi/services/users/by_username?username=" + ocUser + "&fields=uuid&consumer_key=8YV657YqzqDcVC3QC9wM");
	        is = url.openStream(); 
	        br = new BufferedReader(new InputStreamReader(is));

	        line = br.readLine(); //Antwort speichern (nur eine Zeile zurückgeliefert)
	        
	        is.close();
	    } catch (MalformedURLException e) {
	        if (debug) e.printStackTrace();
	        return false;
	    } catch (IOException e) {
	    	javax.swing.JOptionPane.showMessageDialog(null,
	    			"Benutzer nicht gefunden!\nBitte Aufrufparameter prüfen!",
	    			"Ungültiger Benutzername",
	    			JOptionPane.INFORMATION_MESSAGE);
	        if (debug) System.out.println("Benutzer nicht gefunden! Bitte Aufrufparameter prüfen!");
	        if (debug) System.out.println(line);
	        return false;
	    }
	    if (debug) System.out.println(line);
	    try {
	    	//Rückgabestring auf String "uuid" überprüfen
	    	if (!line.substring(2,6).equals("uuid")) return false;
	    } catch (StringIndexOutOfBoundsException e) {
	    	if (debug) e.printStackTrace();
	    	return false;
	    }
	    UUID = line.substring(9,line.length() - 2); //Benutzer-ID extrahieren 
	    if (debug) System.out.println("Benutzer wurde gefunden.");
	    
	    return true;
	}
	
	//Koordinaten des Startpunktes ermitteln
	private boolean getCoordsOfStart() {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line = "";
	    String Start_mod = "";
	    
	    //Umlaute, Leerzeichen ersetzen; ungültige Zeichen entfernen
	    for (int i = 0; i < Start.length(); i++) {
	    	switch (Start.charAt(i)) {
	    		case ' ': Start_mod += "+"; break;
	    		case 'Ä':
	    		case 'ä': Start_mod += "ae"; break;
	    		case 'Ö':
	    		case 'ö': Start_mod += "oe"; break;
	    		case 'Ü':
	    		case 'ü': Start_mod += "ue"; break;
	    		case 'ß': Start_mod += "ss"; break;
	    		default: if ((((Start.charAt(i) >= 'A') && Start.charAt(i) <= 'Z'))
    							|| ((Start.charAt(i) >= 'a') && (Start.charAt(i) <= 'z'))
    							|| ((Start.charAt(i) >= '0') && (Start.charAt(i) <= '9'))) {
							Start_mod += Start.charAt(i);
					 	 };
	    	}
	    }
	    if (debug) System.out.println(Start_mod);

	    try {
	        url = new URL("http://nominatim.openstreetmap.org/search?q=" + Start_mod + "&format=json");
	        is = url.openStream();
	        br = new BufferedReader(new InputStreamReader(is));

	        line = br.readLine();
	        
	        is.close();
	    } catch (MalformedURLException e) {
	    	if (debug) e.printStackTrace();
	        return false;
	    } catch (IOException e) {
	    	if (debug) e.printStackTrace();
	    	if (debug) System.out.println(line);
	        return false;
	    }
	    if (debug) System.out.println(line);
	    try {
	    	int i = 0;
		    boolean gefunden = false;
		  //nach erstem Auftauchen des "lat"-Strings suchen
		    while (i < line.length() && !gefunden) {
		    	if (line.substring(i, i + 6).equals("lat\":\"")) {
		    		gefunden = true;
		    	} else {
		    		i++;
		    	}
		    }
		    int j = i + 7;
		    //Ende der lat-Koordinate suchen
		    while (line.charAt(j) != '"') {
		    	j++;
		    }
		    latS = line.substring(i + 6, j);
		    int k = j + 9;
		    //Ende der lng-Koordinate suchen
		    while (line.charAt(k) != '"') {
		    	k++;
		    }
		    lngS = line.substring(j+9, k);
	    } catch (StringIndexOutOfBoundsException e) {
	    	if (debug) e.printStackTrace();
	    	return false;
	    }
	    if (debug) System.out.println(latS + "," + lngS);
	    return true;
	}
	
	private boolean getCoordsOfZiel() {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line = "";
	    String Ziel_mod = "";
	    
	    for (int i = 0; i < Ziel.length(); i++) {
	    	switch (Ziel.charAt(i)) {
	    		case ' ': Ziel_mod += "+"; break;
	    		case 'Ä':
	    		case 'ä': Ziel_mod += "ae"; break;
	    		case 'Ö':
	    		case 'ö': Ziel_mod += "oe"; break;
	    		case 'Ü':
	    		case 'ü': Ziel_mod += "ue"; break;
	    		case 'ß': Ziel_mod += "ss"; break;
	    		default: if ((((Ziel.charAt(i) >= 'A') && Ziel.charAt(i) <= 'Z'))
    							|| ((Ziel.charAt(i) >= 'a') && (Ziel.charAt(i) <= 'z'))
    							|| ((Ziel.charAt(i) >= '0') && (Ziel.charAt(i) <= '9'))) {
							Ziel_mod += Ziel.charAt(i);
					 	 };
	    	}
	    }
	    if (debug) System.out.println(Ziel_mod);

	    try {
	        url = new URL("http://nominatim.openstreetmap.org/search?q=" + Ziel_mod + "&format=json");
	        is = url.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is));

	        line = br.readLine();
	        
	        is.close();
	    } catch (MalformedURLException e) {
	    	if (debug) e.printStackTrace();
	        return false;
	    } catch (IOException e) {
	    	if (debug) e.printStackTrace();
	        return false;
	    }
	    if (debug) System.out.println(line);
	    try {
	    	int i = 0;
		    boolean gefunden = false;
		    while (i < line.length() && !gefunden) {
		    	if (line.substring(i, i + 6).equals("lat\":\"")) {
		    		gefunden = true;
		    	} else {
		    		i++;
		    	}
		    }
		    int j = i + 7;
		    while (line.charAt(j) != '"') {
		    	j++;
		    }
		    latZ = line.substring(i + 6, j);
		    int k = j + 9;
		    while (line.charAt(k) != '"') {
		    	k++;
		    }
		    lngZ = line.substring(j+9, k);
	    } catch (StringIndexOutOfBoundsException e) {
	    	if (debug) e.printStackTrace();
	    	return false;
	    }
	    if (debug) System.out.println(latZ + "," + lngZ);
	    return true;
	}
	
	//KML-Route herunterladen
	private boolean downloadKMLRoute() {
		try {
			//Route berechnen und in Datei herunterladen
			URL url = new URL("http://www.yournavigation.org/api/1.0/gosmore.php?flat=" + latS + "&flon=" + lngS + "&tlat=" + latZ + "&tlon=" + lngZ + "&v=motorcar&fast=1");
	        File file = new File(System.getProperty("user.home") + File.separator + "occar" + File.separator + "route.kml");
	        if (file.exists()) {
	        	file.delete(); //eventuell existierende Datei gleichen Namens löschen
	        }
			FileUtils.copyURLToFile(url, file, 0, 0); //Datei speichern
		} catch (MalformedURLException e) {
			if (debug) e.printStackTrace();
			return false;
		} catch (IOException e) {
			if (debug) e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//Routenkoordinaten aus KML-Datei auslesen
	private boolean KML2Array() {
		int stelleImArray = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home") + File.separator + "occar" + File.separator + "route.kml"));
		    String line = br.readLine();
		    int anzahlZeilen = 0;

		    while (line != null) { //solange noch nicht Dateiende erreicht
		        if (line.charAt(0) != '<' && line.charAt(0) != ' ') { //linksbündige Zeile suchen
		        	if (anzahlZeilen % 10 == 0) { //nur jede 10. Zeile im Array speichern
		        		coords_list[stelleImArray] = line;
		        		stelleImArray++;
		        	}
		        	anzahlZeilen++;
		        }
		        line = br.readLine(); //neue Zeile lesen
		    }
		    
		    br.close();
		} catch (IOException e) {
			if (debug) e.printStackTrace();
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			if (debug) e.printStackTrace();
			return false;
		}
		
		if (debug) {
			System.out.println(stelleImArray);
			/*for (int i = 0; i < stelleImArray; i++) {
				System.out.println(coords_list[i]);
			}*/
		}
		
		anzahlAbfragen = stelleImArray;
		return true;
	}
	
	//Routenkoordinaten aus GPX-Datei auslesen
	private boolean GPX2Array() {
		int stelleImArray = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(loadpath));
		    String line = br.readLine();
		    int anzahlZeilen = 0;
		    while (line != null) { //solange noch nicht Dateiende erreicht
		    	int startpunkt = 0;
				
				for (int i = 0; i < line.length() - 8; i++) {
					if (line.substring(i, i+9).equals("trkpt lat")) { //nach Trackpoint suchen
						if (anzahlZeilen % 10 == 0) { //nur jeden 10. Trackpoint im Array speichern
							startpunkt = i + 12;
			    			int endpunkt = startpunkt;
			    			//Ende des 2. Koordinatenpaarelements suchen
							while (!line.substring(endpunkt, endpunkt+7).equals("\" lon=\"")) {
								endpunkt++;
							}
							String teil2 = line.substring(startpunkt-1, endpunkt);
							//System.out.println(teil2);
							endpunkt += 6;
							startpunkt = endpunkt;
							//Ende des 1. Koordinatenpaarelements suchen
							while ((!line.substring(endpunkt, endpunkt+3).equals("\"/>"))) {
								endpunkt++;
							}
							String teil1 = line.substring(startpunkt + 1, endpunkt);
							//System.out.println(teil1);
							
							coords_list[stelleImArray] = teil1 + "," + teil2;
							stelleImArray++;
						}
						anzahlZeilen++;
		    		}
					
		    	}
		        line = br.readLine(); //nächste Zeile lesen
		    }
		    
		    br.close();
		} catch (IOException e) {
			if (debug) e.printStackTrace();
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			if (debug) e.printStackTrace();
			return false;
		} catch (StringIndexOutOfBoundsException e) {
			if (debug) e.printStackTrace();
			return false;
		}
		
		if (debug) {
			System.out.println(stelleImArray);
			/*for (int i = 0; i < stelleImArray; i++) {
				System.out.println(coords_list[i]);
			}*/
		}
		
		anzahlAbfragen = stelleImArray;
		return true;
	}
	
	//Radius überprüfen
	private boolean checkRadius() {
		if (Radius < 0.1) { //Radius groß genug
			javax.swing.JOptionPane.showMessageDialog(null, "Der eingegebene Radius ist zu klein!");
			return false;
		}
		if (Radius > 10) { //Radius klein genug
			javax.swing.JOptionPane.showMessageDialog(null, "Der eingegebene Radius ist zu groß!");
			return false;
		}
		return true;
	}
	
	//Caches entlang der Route suchen
	private boolean requestCaches() {
		String arten = "";
		
		//gewünschte Cachearten auslesen
		if (!alleArten) {
			//bitweiser Vergleich mit entsprechender Maske
			if ((Arten & (1<<0)) == (1<<0)) {
				if (arten.equals("")) {
					arten += "Traditional";
				} else {
					arten += "|Traditional";
				}
			}
			if ((Arten & (1<<1)) == (1<<1)) {
				if (arten.equals("")) {
					arten += "Multi";
				} else {
					arten += "|Multi";
				}
			}
			if ((Arten & (1<<2)) == (1<<2)) {
				if (arten.equals("")) {
					arten += "Quiz";
				} else {
					arten += "|Quiz";
				}
			}
			if ((Arten & (1<<3)) == (1<<3)) {
				if (arten.equals("")) {
					arten += "Virtual";
				} else {
					arten += "|Virtual";
				}
			}
			if ((Arten & (1<<4)) == (1<<4)) {
				if (arten.equals("")) {
					arten += "Event";
				} else {
					arten += "|Event";
				}
			}
			if ((Arten & (1<<5)) == (1<<5)) {
				if (arten.equals("")) {
					arten += "Webcam";
				} else {
					arten += "|Webcam";
				}
			}
			if ((Arten & (1<<6)) == (1<<6)) {
				if (arten.equals("")) {
					arten += "Moving";
				} else {
					arten += "|Moving";
				}
			}
			if ((Arten & (1<<7)) == (1<<7)) {
				if (arten.equals("")) {
					arten += "Math/Physics";
				} else {
					arten += "|Math/Physics";
				}
			}
			if ((Arten & (1<<8)) == (1<<8)) {
				if (arten.equals("")) {
					arten += "Drive-In";
				} else {
					arten += "|Drive-In";
				}
			}
			if ((Arten & (1<<9)) == (1<<9)) {
				if (arten.equals("")) {
					arten += "Other";
				} else {
					arten += "|Other";
				}
			}
		}
		
		int stelleImArray = 0;
		String[] alle = new String[anzahlAbfragen];
		for (int i = 0; i < anzahlAbfragen; i++) {
			URL url;
		    InputStream is = null;
		    BufferedReader br;
		    String line = "";
		    
		    //die Koordinaten werden genau verkehrt herum gespeichert und müssen umgedreht werden
		    int j = 0;
		    String coords_fehler = coords_list[i];
		    while (coords_fehler.charAt(j) != ',') {
		    	j++;
		    }
		    String coords = coords_fehler.substring(j+1, coords_fehler.length())
		    		+ "|" + coords_fehler.substring(0, j);
		    //System.out.println(coords);
		    
		    try {
		    	//Abfrage durchführen (Mittelpunkt-Suche mit Radius)
		    	if (alleArten) {
		    		url = new URL("http://www.opencaching.de/okapi/services/caches/search/nearest?center="
		    				+ coords
		    				+ "&radius="
		    				+ df_radius.format(Radius)
		    				+ "&difficulty="
		    				+ Difficulty
		    				+ "&terrain="
		    				+ Terrain
		    				+ "&status=Available&consumer_key=8YV657YqzqDcVC3QC9wM");
		    	} else {
		    		url = new URL("http://www.opencaching.de/okapi/services/caches/search/nearest?center="
		    				+ coords
		    				+ "&radius="
		    				+ df_radius.format(Radius)
		    				+ "&type="
		    				+ arten
		    				+ "&difficulty="
		    				+ Difficulty
		    				+ "&terrain="
		    				+ Terrain
		    				+ "&status=Available&consumer_key=8YV657YqzqDcVC3QC9wM");
		    	}
		    	//System.out.println(url);
		        is = url.openStream();
		        br = new BufferedReader(new InputStreamReader(is));

		        line = br.readLine();
		        
		        is.close();
		    } catch (MalformedURLException e) {
		    	if (debug) e.printStackTrace();
		        return false;
		    } catch (IOException e) {
		    	if (debug) e.printStackTrace();
		        return false;
		    }
		    //System.out.println(line);
		    
		    //prüfen, ob Dose gefunden
		    if (line.length() < 30) {
		    	//keine Dose in diesem Bereich
		    } else {
		    	String a = line.substring(13, line.length() - 16);
			    String b = a.replace("\"", ""); //Anführungszeichen durch Leerzeichen ersetzen
			    b = b.replace(",", " "); //Kommas durch Leerzeichen ersetzen
			    alle[stelleImArray] = b; //Ergebnis der Abfrage zum Array hinzufügen
			    stelleImArray++;
		    }
		}
		
		if (debug) {
			for (int i = 0; i < stelleImArray; i++) {
				System.out.println(alle[i]);
			}
			System.out.println("---");
		} 
		
		//Duplikate filtern und OC-Codes feldweise in Array schreiben
		ohneDuplikate = 0;
		for (int i = 0; i < stelleImArray; i++) {
			int startpunkt = 0;
			int endpunkt = 0;
			String aktuell = alle[i];
			//System.out.println(aktuell);
			while (endpunkt < aktuell.length()) {
				if (aktuell.charAt(endpunkt) == ' ') {
					if (ohneDuplikate == 0) {
						caches[0] = aktuell.substring(startpunkt, endpunkt);
						startpunkt = endpunkt + 1;
						ohneDuplikate++;
					} else {
						String teilstring = aktuell.substring(startpunkt, endpunkt);
						boolean schon_vorhanden = false;
						for (int j = 0; j < ohneDuplikate; j++) {
							if (teilstring.equals(caches[j])) {
								schon_vorhanden = true;
								startpunkt = endpunkt + 1;
							}
						}
						if (!schon_vorhanden) {
							caches[ohneDuplikate] = teilstring;
							startpunkt = endpunkt + 1;
							ohneDuplikate++;
						}
					}
				}
				endpunkt++;
			}
			String teilstring = aktuell.substring(startpunkt, endpunkt);
			boolean schon_vorhanden = false;
			for (int j = 0; j < ohneDuplikate; j++) {
				if (teilstring.equals(caches[j])) {
					schon_vorhanden = true;
					startpunkt = endpunkt + 1;
				}
			}
			if (!schon_vorhanden) {
				caches[ohneDuplikate] = teilstring;
				startpunkt = endpunkt + 1;
				ohneDuplikate++;
			}
		}
		
		if (debug) System.out.println("Gefundene Listings ohne Duplikate: " + ohneDuplikate);
		if (debug) {
			for (int i = 0; i < ohneDuplikate; i++) {
				System.out.print(caches[i] + " , ");
			}
		}
		
		return true;
	}
	
	//GPX-Dateien mit den gefundenen und gefilterten Caches herunterladen
	private boolean downloadCaches() {
		int anzahlAufrufe = ohneDuplikate / 500 + 1;
		int ende = 0;
		if (ohneDuplikate < 500) {
			ende = ohneDuplikate;
		} else {
			ende = 500;
		}
		for (int i = 0; i < anzahlAufrufe; i++) {
			
			String codes_aufruf = caches[i*500];
			for (int j = i * 500 + 1; j < ende; j++) {
				codes_aufruf = codes_aufruf + "|" + caches[j];
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
			Date date = new Date();
			String outputFile = dateFormat.format(date) + "PQ.gpx";
			
			if (debug) System.out.println(codes_aufruf);
			
			try {
				URL url = new URL("http://www.opencaching.de/okapi/services/caches/formatters/gpx?cache_codes=" + codes_aufruf + "&consumer_key=8YV657YqzqDcVC3QC9wM&ns_ground=true&latest_logs=true&mark_found=true&user_uuid=" + UUID);
		        File file = new File(System.getProperty("user.home") + File.separator + "occar" + File.separator + outputFile);
				FileUtils.copyURLToFile(url, file, 0, 0);
			} catch (MalformedURLException e) {
				if (debug) e.printStackTrace();
				return false;
			} catch (IOException e) {
				if (debug) e.printStackTrace();
				return false;
			}
			
			if (!password.equals("")) { //E-Mail-Versand gewünscht
				boolean erfolg = false;
				erfolg = email.sendMailWithAttachment(host, port,
					sender, receiver,
					password,
					outputFile,
					subject, body,
					debug);
				if (!password.equals("") && erfolg) {
					lblFortschritt.setText("Die Datei(en) wurden im Verzeichnis abgelegt und per E-Mail versendet.");
				}
				if (!password.equals("") && !erfolg) {
					lblFortschritt.setText("Fehler beim Versenden der Datei(en)!");
				}
			} else { //kein E-Mail-Versand gewünscht
				lblFortschritt.setText("Die Datei " + outputFile + " wurde gespeichert!");
			}
			
			if (ende < ohneDuplikate) {
				if ((ende + 500) < ohneDuplikate) {
					ende += 500;
				} else {
					ende = ohneDuplikate;
				}
			}
		}
		return true;
	}
	
	//auf neue Programmversion prüfen
	private boolean checkNewVersion() {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line = "";
	    
	    try {
	    	//Dateiinhalt der Versionsdatei von Github ermitteln
	        url = new URL("https://raw.githubusercontent.com/FriedrichFroebel/oc_car-gui/master/version");
	        is = url.openStream();
	        br = new BufferedReader(new InputStreamReader(is));

	        line = br.readLine();
	        
	        if (debug) System.out.println("Inhalt Versionscheck: " + line);
	        
	        is.close();
	    } catch (MalformedURLException e) {
	    	if (debug) e.printStackTrace();
	    	return false;
	    } catch (IOException e) {
	    	if (debug) e.printStackTrace();
	        return false;
	    }
	    
	    if (!line.equals(version)) { //Zeileninhalt stimmt nicht mit Versionsangabe des Programms überein
	    	//Information anzeigen
	    	javax.swing.JOptionPane.showMessageDialog(null,
	    			"Neue Version verfügbar:\n"
	    			+ "https://github.com/FriedrichFroebel/oc_car-gui/releases",
	    			"Versionshinweis",
	    			JOptionPane.INFORMATION_MESSAGE);
	    	return true;
	    } else {
	    	if (debug) System.out.println("Es wird bereits die aktuellste Version verwendet."); 
	    }
	    
	    return false;
	}
	
	//Aufruf der einzelnen Elemente der Suche
	private boolean sucheStarten() {
		
		if (debug) System.out.println(System.getProperty("user.home"));
				
		/*
		 * die eigentlich wichtigen Schritte
		 * nur bei Gültigkeit des vorhergehenden wird der nächste ausgeführt
		 * sonst bricht das Programm ab und gibt bei aktivierter Debug-Flag
		 * eine Hinweismeldung aus, wo der Fehler aufgetreten ist
		 */
		if (getUUID()) {
			if (getCoordsOfStart() && getCoordsOfZiel()) {
				if (loadGPX) {
					if (GPX2Array()) {
						if (checkRadius()) {
							if (requestCaches()) {
								if (!downloadCaches()) {
									if (debug) System.out.println("Abbruch: downloadCaches()");
								}
							} else {
								if (debug) System.out.println("Abbruch: requestCaches()");
							}
						} else {
							if (debug) System.out.println("Abbruch: checkRadius()");
						}
					} else {
						if (debug) System.out.println("Abbruch: GPX2Array()");
					}
				} else {
					if (downloadKMLRoute()) {
						if (KML2Array()) {
							if (checkRadius()) {
								if (requestCaches()) {
									if (!downloadCaches()) {
										if (debug) System.out.println("Abbruch: downloadCaches()");
										return false;
									}
								} else {
									if (debug) System.out.println("Abbruch: requestCaches()");
									return false;
								}
							} else {
								if (debug) System.out.println("Abbruch: checkRadius()");
								return false;
							}
						} else {
							if (debug) System.out.println("Abbruch: KML2Array()");
							return false;
						}
					} else {
						if (debug) System.out.println("Abbruch: downloadKMLRoute()");
						return false;
					}
				}
			} else {
				if (debug) System.out.println("Abbruch: getCoords()");
				return false;
			}
		} else {
			if (debug) System.out.println("Abbruch: getUUID()");
			return false;
		}
		
		return true;
	}
}