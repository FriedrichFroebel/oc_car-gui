import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class sendEmail {
	
	//Servereinstellungen
	private Properties serversettings = new Properties();
	
	//Servereinstellungen setzen
	private void setServerSettings(String host, String port) {
		serversettings.put("mail.smtp.auth", "true");
		serversettings.put("mail.smtp.starttls.enable", "true");
		serversettings.put("mail.smtp.host", host);
		serversettings.put("mail.smtp.port", port);
	}
	
	//E-Mail mit Anhang versenden
	public boolean sendMailWithAttachment(String server, String serverport,
			String sender, String receiver,
			String password,
			String filename,
			String subject, String body,
			boolean debug) {
		
		setServerSettings(server, serverport); //Servereinstellungen konfigurieren
		
		//Session starten
		Session session = Session.getInstance(serversettings,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(sender, password);
					}
				  });
		
		try {
			//MimeMessage erstellen
			MimeMessage message = new MimeMessage(session);
			
			//Absender festlegen
			message.setFrom(new InternetAddress(sender));
			
			//Empfänger festlegen
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(receiver));
			
			//Betreff festlegen
			message.setSubject(subject);
			
			//Nachricht anlegen
			BodyPart messageBodyPart = new MimeBodyPart();
			
			//Nachrichtentext hinzufügen
			messageBodyPart.setText(body);
			
			//für Anhang ist Multipart-Message notwendig
			Multipart multipart = new MimeMultipart();
			
			//Textteil hinzufügen
			multipart.addBodyPart(messageBodyPart);
			
			//Anhang hinzufügen
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(
					System.getProperty("user.home") + File.separator + "occar" + File.separator + filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			messageBodyPart.setDisposition(Part.ATTACHMENT);
			messageBodyPart.setHeader("Content-Transfer-Encoding", "base64");
			multipart.addBodyPart(messageBodyPart);
			
			//Zeitpunkt des Sendens festlegen
			message.setSentDate(new Date());
			
			//gesamte Nachricht senden (inklusive Anhang)
			message.setContent(multipart);
			
			//Nachricht senden
			Transport.send(message);
			
			if (debug) System.out.println("Die Datei " + filename + " wurde im Verzeichnis abgelegt und per E-Mail versendet.");
		} catch (MessagingException e) {
			if (debug) e.printStackTrace();
			System.out.println("Fehler beim Versenden der Datei " + filename + "!");
			return false;
		}
		return true;
	}

}
