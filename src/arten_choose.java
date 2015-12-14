import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class arten_choose extends JFrame {
	
	private int Auswahl = 0; //ausgewählte Cachearten
	private oc_car zugriff = new oc_car(); //Cachearten auch in der Hauptklasse verändern

	private JPanel contentPane;

	//Frame erstellen
	public arten_choose() {
		
		int predefined = zugriff.getCachearten(); //bisherige Konfiguration ermitteln
		
		//allgemeine Einstellungen des Frames
		setResizable(false);
		setTitle("Cachearten ausw\u00E4hlen");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(175, 175, 360, 205);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Checkboxen erstelln
		//wenn beim bitweisen Vergleich die entsprechende Maske passt, wird die Box markiert
		
		JCheckBox boxTradi = new JCheckBox("Traditional Caches");
		boxTradi.setBounds(10, 10, 160, 25);
		contentPane.add(boxTradi);
		if ((predefined & (1<<0)) == (1<<0)) boxTradi.setSelected(true);
		
		JCheckBox boxMulti = new JCheckBox("Multicaches");
		boxMulti.setBounds(10, 35, 160, 25);
		contentPane.add(boxMulti);
		if ((predefined & (1<<1)) == (1<<1)) boxMulti.setSelected(true);
		
		JCheckBox boxMystery = new JCheckBox("R\u00E4tselcaches");
		boxMystery.setBounds(10, 60, 160, 25);
		contentPane.add(boxMystery);
		if ((predefined & (1<<2)) == (1<<2)) boxMystery.setSelected(true);
		
		JCheckBox boxVirtual = new JCheckBox("Virtuelle Caches");
		boxVirtual.setBounds(10, 85, 160, 23);
		contentPane.add(boxVirtual);
		if ((predefined & (1<<3)) == (1<<3)) boxVirtual.setSelected(true);
		
		JCheckBox boxEvent = new JCheckBox("Eventcaches");
		boxEvent.setBounds(10, 110, 160, 23);
		contentPane.add(boxEvent);
		if ((predefined & (1<<4)) == (1<<4)) boxEvent.setSelected(true);
		
		JCheckBox boxWebcam = new JCheckBox("Webcamcaches");
		boxWebcam.setBounds(175, 10, 175, 23);
		contentPane.add(boxWebcam);
		if ((predefined & (1<<5)) == (1<<5)) boxWebcam.setSelected(true);
		
		JCheckBox boxMoving = new JCheckBox("Moving Caches");
		boxMoving.setBounds(175, 35, 175, 23);
		contentPane.add(boxMoving);
		if ((predefined & (1<<6)) == (1<<6)) boxMoving.setSelected(true);
		
		JCheckBox boxMath = new JCheckBox("Mathe-/Physikcaches");
		boxMath.setBounds(175, 60, 175, 23);
		contentPane.add(boxMath);
		if ((predefined & (1<<7)) == (1<<7)) boxMath.setSelected(true);
		
		JCheckBox boxDrive = new JCheckBox("Drive-In-Caches");
		boxDrive.setBounds(175, 85, 175, 23);
		contentPane.add(boxDrive);
		if ((predefined & (1<<8)) == (1<<8)) boxDrive.setSelected(true);
		
		JCheckBox boxOther = new JCheckBox("Sonstige");
		boxOther.setBounds(175, 110, 175, 23);
		contentPane.add(boxOther);
		if ((predefined & (1<<9)) == (1<<9)) boxOther.setSelected(true);
		
		//Auswahl durch Klick auf Button übernehmen
		JButton btnUebernehmen = new JButton("\u00DCbernehmen");
		btnUebernehmen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Auswahl mittels Bitmasken zur Integer-Variable hinzufügen
				//Zustände von 0 bis 1023 möglich
				if (boxTradi.isSelected()) Auswahl += 1<<0;
				if (boxMulti.isSelected()) Auswahl += 1<<1;
				if (boxMystery.isSelected()) Auswahl += 1<<2;
				if (boxVirtual.isSelected()) Auswahl += 1<<3;
				if (boxEvent.isSelected()) Auswahl += 1<<4;
				if (boxWebcam.isSelected()) Auswahl += 1<<5;
				if (boxMoving.isSelected()) Auswahl += 1<<6;
				if (boxMath.isSelected()) Auswahl += 1<<7;
				if (boxDrive.isSelected()) Auswahl += 1<<8;
				if (boxOther.isSelected()) Auswahl += 1<<9;
				//System.out.println(Auswahl);
				zugriff.setCachearten(Auswahl); //im Hauptprogramm geänderten Wert setzen
		        CloseFrame(); //Methodenaufruf zum Schließen des Frames
			}
		});
		btnUebernehmen.setBounds(110, 140, 140, 23);
		contentPane.add(btnUebernehmen);
	}
	
	//Frame schließen (nur über externe Funktion in der gleichen Klasse möglich)
	public void CloseFrame(){
	    super.dispose();
	}
}
