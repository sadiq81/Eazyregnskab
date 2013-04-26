package dk.eazyit.eazyregnskab;

import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import dk.eazyit.eazyregnskab.dao.interfaces.*;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.web.app.WicketApplication;
import dk.eazyit.eazyregnskab.web.app.front.HomePage;
import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/applicationContext-test.xml",
        "classpath:/spring-security.xml",
        "classpath:/dataSource-test.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TestBaseCase extends TestCase {


    @Autowired
    protected WicketTester tester;

    @Autowired
    private ApplicationContext ctx;

    @Autowired()
    private WicketApplication wicketApplication;

    @Autowired
    protected LegalEntityDAO legalEntityDAO;

    @Autowired
    protected AppUserDAO appUserDAO;

    @Autowired
    protected DailyLedgerDAO dailyLedgerDAO;

    @Autowired
    protected LegalEntityAccessDAO legalEntityAccessDAO;

    @Autowired
    protected  AppUserRoleDAO appUserRoleDAO;

    @Autowired
    protected LegalEntityService legalEntityService;

    @Autowired
    protected LoginService loginService;

    @Autowired
    protected FinanceAccountService financeAccountService;

    @Before
    public void BaseCaseSetUp() {

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("-656894668", "anonymousUser", simpleGrantedAuthorities));

//        tester = new WicketTester(wicketApplication);

    }

    @Test
    public void testRenderHomePage() {
        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);
        tester.assertComponent("topMenu", Navbar.class);
    }


}