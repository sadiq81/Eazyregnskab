package dk.eazyit.eazyregnskab.system;

import org.apache.catalina.realm.JDBCRealm;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import java.security.Principal;

/**
 * @author
 */
public class EazyregnskabRealm extends JDBCRealm {

    private static final Log log = LogFactory.getLog(EazyregnskabRealm.class);

    private static final ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

    @Override
    protected String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public synchronized Principal authenticate(String username, String credentials) {
        String unDigestedPassword = credentials;
        String digestedPassword = encoder.encodePassword(unDigestedPassword, username);
        return super.authenticate(username, digestedPassword);
    }

}
