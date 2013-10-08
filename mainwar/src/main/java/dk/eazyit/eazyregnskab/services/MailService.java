package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.util.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;


/**
 * @author
 */
@Service(value = "MailService")
public class MailService {

    @Autowired
    FileReader fileReader;

    final Logger LOG = LoggerFactory.getLogger(MailService.class);

    private static final String SMTP_HOST_NAME = "smtp.gigahost.dk";
    private static final String SMTP_AUTH_USER = "do-not-reply@eazyregnskab.dk";
    private static final String SMTP_AUTH_PWD = "l63dm28u";
    private static final String ID_PHRASE = "*|ID|*";
    private static final String YEAR_PHRASE = "*|CURRENT_YEAR|*";

    public void sendConfirmationEmail(String email, String UUID) {

        LOG.info("Sending confirmation email to " + email + " with UUID " + UUID);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.MailService", org.apache.wicket.Session.get().getLocale());
        String content = fileReader.readFromTxtFile(bundle.getString("message.file"));
        content = content.replace(ID_PHRASE, UUID);
        content = content.replace(YEAR_PHRASE, (Calendar.getInstance().get(Calendar.YEAR)) + "");

        if (System.getProperty("local") == null) {
            try {
                Properties props = new Properties();
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", SMTP_HOST_NAME);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");


                Authenticator auth = new SMTPAuthenticator();
                Session mailSession = Session.getDefaultInstance(props, auth);
                Transport transport = mailSession.getTransport();

                MimeMessage message = new MimeMessage(mailSession);
                message.setSubject(bundle.getString("subject"));
                message.setSentDate(new Date());


                message.setContent(content, "text/html; charset=utf-8");
                message.setFrom(new InternetAddress("do-not-reply@eazyregnskab.dk"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                transport.connect();
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                transport.close();
            } catch (MessagingException e) {
                LOG.error("Mail to user with email " + email + " was not send due to error");
                //TODO should be thrown all the way to signup page.
            }
        }
        LOG.info("Sending confirmation email to " + email + " with UUID " + UUID);
    }

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
}
