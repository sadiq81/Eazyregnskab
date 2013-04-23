package dk.eazyit.eazyregnskab.wicket.securepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import org.junit.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public abstract class TestLoggedInPage extends TestBaseCase {

    @Before
    public void LoggedInPageSetUp() {

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "test", simpleGrantedAuthorities));

    }


}
