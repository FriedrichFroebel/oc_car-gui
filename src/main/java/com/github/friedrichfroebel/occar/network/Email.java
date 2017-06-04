package com.github.friedrichfroebel.occar.network;

import com.github.friedrichfroebel.occar.config.Configuration;

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

        Session session = Session.getInstance(serverSettings,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication
                        getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                Configuration.getEmailSender(),
                                Configuration.getEmailPassword());
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(
                    Configuration.getEmailSender()));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(Configuration.getEmailRecipient()));
            message.setSubject(Configuration.getEmailSubject());

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(Configuration.getEmailBody());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
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
