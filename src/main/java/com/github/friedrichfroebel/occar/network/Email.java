package com.github.friedrichfroebel.occar.network;

import com.github.friedrichfroebel.occar.config.Configuration;

import java.util.Date;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

/**
 * This class handles the email transport.
 */
public class Email {

    /**
     * The settings for the email server.
     */
    private static Properties serverSettings;

    /**
     * Init the server settings.
     */
    private static void setServerSettings() {
        serverSettings = new Properties();
        serverSettings.put("mail.smtp.auth", "true");
        serverSettings.put("mail.smtp.starttls.enable", "true");
        serverSettings.put("mail.smtp.host", Configuration.getEmailHost());
        serverSettings.put("mail.smtp.port", Configuration.getEmailPort());
    }

    /**
     * Send an email with the given attachment.
     *
     * @param filename The attachment.
     * @return {@code True} if email could be sent, {@code false} otherwise.
     */
    public static boolean sendEmailWithAttachment(String filename) {
        setServerSettings();

        final Session session = Session.getInstance(serverSettings,
                new Authenticator() {
                    protected PasswordAuthentication
                        getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                Configuration.getEmailSender(),
                                Configuration.getEmailPassword());
                    }
                });

        try {
            final MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(
                    Configuration.getEmailSender()));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(Configuration.getEmailRecipient()));
            message.setSubject(Configuration.getEmailSubject());

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(Configuration.getEmailBody());

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            final DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            messageBodyPart.setDisposition(Part.ATTACHMENT);
            messageBodyPart.setHeader("Content-Transfer-Encoding", "base64");
            multipart.addBodyPart(messageBodyPart);

            message.setSentDate(new Date());
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException exception) {
            return false;
        }
        return true;
    }
}
