import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

public class progressBar extends JFrame {

	private JPanel contentPane;
	private JProgressBar progressBar;

	//Frame erstellen
	public progressBar() {
		setResizable(false);
		setTitle("Fortschritt"); //Titel
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 200, 350, 80);
		getContentPane().setLayout(null);
		
		//Fortschrittsbalken
		progressBar = new JProgressBar(0,100);
		progressBar.setBounds(10, 10, 330, 40);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		//Umrandung
		Border border = BorderFactory.createTitledBorder("Abfragen werden ausgeführt ...");
		progressBar.setBorder(border);
		getContentPane().add(progressBar, BorderLayout.NORTH);
		
		contentPane = new JPanel();
	}
	
	//Anzeige aktualisieren
	public void updatebar(int wert) {
		progressBar.setValue(wert); //Wert setzen
		progressBar.paint(progressBar.getGraphics()); //Aktualisierung der Anzeige erzwingen
	}
}
