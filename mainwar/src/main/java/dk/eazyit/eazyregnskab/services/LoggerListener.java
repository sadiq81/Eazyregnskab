package dk.eazyit.eazyregnskab.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * @author Trifork
 */
public class LoggerListener implements ApplicationListener<AbstractAuthenticationEvent> {


    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {

        if (event instanceof AuthenticationSuccessEvent) {

            final Log logger = LogFactory.getLog(this.getClass());
            User user = (User) event.getAuthentication().getPrincipal();
            WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
            logger.info("Successful login event of user:" + user.getUsername() + " from: " + details.getRemoteAddress());
        }
    }
}
