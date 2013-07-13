package dk.eazyit.eazyregnskab.wicket.securepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.domain.*;
import org.apache.wicket.Session;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author
 */
@Transactional
public abstract class TestLoggedInPage extends TestBaseCase {


    @Autowired
    private JpaTransactionManager jpaTransactionManager;

    @Before
    @Rollback
    @Transactional
    public void LoggedInPageSetUp()  {

        AppUser appUser = new AppUser("test", "test", true, "test","test");
        appUser.setId(1L);
        appUserDAO.save(appUser);
        HashSet<AppUserRole> appUserRoles = new HashSet<AppUserRole>();
        AppUserRole appUserRole = new AppUserRole(appUser, Authority.ROLE_USER);
        appUserRole.setId(1L);
        appUserRoleDAO.save(appUserRole);
        appUserRoles.add(appUserRole);
        appUser.setAppUserRoles(appUserRoles);

        Session.get().setAttribute(AppUser.ATTRIBUTE_NAME, appUser);

        LegalEntity legalEntity = new LegalEntity("test", "12345678", "road 1234", "0000", Country.DK, MoneyCurrency.DKK);
        legalEntity.setId(1L);
        legalEntityDAO.save(legalEntity);
        Session.get().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);


        DailyLedger dailyLedger = new DailyLedger("test", legalEntity);
        dailyLedger.setId(1L);
        dailyLedgerDAO.save(dailyLedger);
        Session.get().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);

        LegalEntityAccess legalEntityAccess = new LegalEntityAccess(appUser, legalEntity);
        legalEntityAccess.setId(1L);
        legalEntityAccessDAO.save(legalEntityAccess);

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "test", simpleGrantedAuthorities));


    }


}
