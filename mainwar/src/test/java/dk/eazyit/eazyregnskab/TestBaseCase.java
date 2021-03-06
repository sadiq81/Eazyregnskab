package dk.eazyit.eazyregnskab;

import dk.eazyit.eazyregnskab.dao.interfaces.*;
import dk.eazyit.eazyregnskab.services.*;
import dk.eazyit.eazyregnskab.web.app.WicketApplication;
import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/applicationContext-test.xml",
        "classpath:/spring-security.xml",
        "classpath:/dataSource-test.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager")
public abstract class TestBaseCase extends TestCase {


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
    protected AppUserRoleDAO appUserRoleDAO;

    @Autowired
    protected LegalEntityService legalEntityService;

    @Autowired
    protected LoginService loginService;

    @Autowired
    protected FinanceAccountService financeAccountService;

    @Autowired
    protected DailyLedgerService dailyLedgerService;

    @Autowired
    protected VatTypeService vatTypeService;

    @Autowired
    protected BookingService bookingService;

    @Autowired
    protected BookedFinancePostingDAO bookedFinancePostingDAO;

    @Autowired
    protected DraftFinancePostingDAO draftFinancePostingDAO;

    @Before
    public void BaseCaseSetUp() {

        //TODO login wicket user
//        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
//        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
//        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("-656894668", "anonymousUser", simpleGrantedAuthorities));
    }

}
