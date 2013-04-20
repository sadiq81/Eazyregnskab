package dk.eazyit.eazyregnskab.hibernate;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityAccessDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityDAO;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.wicket.MockWicketApplication;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@ContextConfiguration(locations = {
        "classpath:/applicationContext.xml",
        "classpath:/spring-security.xml",
        "classpath:/dataSource.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestBaseCase {


    protected WicketTester tester;

    protected ApplicationContextMock mockContext;

    @Autowired
    LoginService loginService;
    @Autowired
    AppUserDAO appUserDAO;
    @Autowired
    LegalEntityDAO legalEntityDAO;
    @Autowired
    LegalEntityAccessDAO legalEntityAccessDAO;
    @Autowired
    LegalEntityService legalEntityService;

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
    public void test() {
    }
}
