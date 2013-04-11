package dk.eazyit.eazyregnskab.wicket.securepages;

import dk.eazyit.eazyregnskab.wicket.TestBaseWicketCase;
import org.junit.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class TestLoggedInPage extends TestBaseWicketCase {

    @Before
    public void setupContext() {

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "test",simpleGrantedAuthorities));

    }


}
