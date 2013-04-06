package dk.eazyit.eazyregnskab.wicket;

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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Trifork
 */
@ContextConfiguration({
        "classpath:/applicationContext.xml",
        "classpath:/dataSource.xml",
        "classpath:/spring-security.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUnSecurePages extends TestCase {

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
    public void testRenderingOfPages() {

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.clickLink("signUp");
        tester.assertRenderedPage(SignUpPage.class);

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.clickLink("whatYouGet");
        tester.assertRenderedPage(WhatYouGetPage.class);

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.clickLink("forAdministrators");
        tester.assertRenderedPage(ForAdministratorsPage.class);

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.clickLink("pricing");
        tester.assertRenderedPage(PricingPage.class);

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);


    }

}
