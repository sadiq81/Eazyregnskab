package dk.eazyit.eazyregnskab.hibernate;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityAccessDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityDAO;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
    }

    @Test
    public void test() {
    }
}
