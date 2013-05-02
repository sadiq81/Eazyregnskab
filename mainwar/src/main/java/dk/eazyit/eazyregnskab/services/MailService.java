package dk.eazyit.eazyregnskab.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * @author
 */
@Service
public class MailService {

    final Logger LOG = LoggerFactory.getLogger(MailService.class);


    private static final String SMTP_HOST_NAME = "smtp.gigahost.dk";
    private static final String SMTP_AUTH_USER = "do-not-reply@eazyregnskab.dk";
    private static final String SMTP_AUTH_PWD = "l63dm28u";


    public void SendConfirmationEmail(String email, String UUID) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port","587");

            Authenticator auth = new SMTPAuthenticator();
            Session mailSession = Session.getDefaultInstance(props, auth);
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);
            message.setContent("This is a test " + UUID, "text/plain");
            message.setFrom(new InternetAddress("do-not-reply@eazyregnskab.dk"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email));

            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (Exception e) {
            LOG.error("Mail to user with email " + email + " was not send due to error");
            //TODO should be thrown all the way to signup page.
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
}
