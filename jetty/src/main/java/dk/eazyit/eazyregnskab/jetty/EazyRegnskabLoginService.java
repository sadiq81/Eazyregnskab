package dk.eazyit.eazyregnskab.jetty;

import dk.eazyit.eazyregnskab.security.PasswordEncoder;
import org.eclipse.jetty.security.JDBCLoginService;
import org.eclipse.jetty.server.UserIdentity;

import java.io.IOException;

/**
 * @author
 */
public class EazyRegnskabLoginService extends JDBCLoginService {

    public EazyRegnskabLoginService() throws IOException {
    }

    @Override
    public UserIdentity login(String username, Object credentials) {

        PasswordEncoder passwordEncoder = PasswordEncoder.getInstance();
        String unDigestedPassword = (String) credentials;
        String digestedPassword = passwordEncoder.encode(unDigestedPassword, username);

        return super.login(username, digestedPassword);
    }

}