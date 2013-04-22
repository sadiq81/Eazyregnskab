package dk.eazyit.eazyregnskab.wicket;

import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import dk.eazyit.eazyregnskab.web.app.front.*;
import junit.framework.TestCase;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Trifork
 */
@ContextConfiguration({
        "classpath:/applicationContext-test.xml",
        "classpath:/dataSource-test.xml",
        "classpath:/spring-security.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestBaseWicketCase extends TestCase {

    protected WicketTester tester;

    protected ApplicationContextMock mockContext;

    @Before
    public void setUp() throws Exception {

        MockWicketApplication webapp = new MockWicketApplication();

        tester = new WicketTester(webapp);

        mockContext = ((MockWicketApplication) tester.getApplication())
                .getMockContext();
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("-656894668", "anonymousUser", simpleGrantedAuthorities));

    }

    @Test
    public void testRenderingOfBasePage() {

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.assertComponent("topMenu", Navbar.class);

    }

}
