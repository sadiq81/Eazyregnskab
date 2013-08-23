package dk.eazyit.eazyregnskab.wicket.securepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.domain.*;
import org.apache.wicket.Session;
import org.junit.Before;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * @author
 */
@Transactional
public abstract class TestLoggedInPage extends TestBaseCase {

    protected AppUser appUser;
    protected LegalEntity legalEntity;
    protected DailyLedger dailyLedger;

    @Before
    public void LoggedInPageSetUp() {

        appUser = new AppUser("test", "test", true, "test", "test");
        appUser = appUserDAO.save(appUser);
        HashSet<AppUserRole> appUserRoles = new HashSet<AppUserRole>();
        AppUserRole appUserRole = new AppUserRole(appUser, Role.USER);
        appUserRole = appUserRoleDAO.save(appUserRole);
        appUserRoles.add(appUserRole);
        appUser.setAppUserRoles(appUserRoles);

        Session.get().setAttribute(AppUser.ATTRIBUTE_NAME, appUser);

        legalEntity = new LegalEntity("test", "12345678", "road 1234", "0000", Country.DNK, MoneyCurrency.DKK);
        legalEntityService.createBaseDataForNewLegalEntity(legalEntity);
        legalEntity = legalEntityDAO.save(legalEntity);
        Session.get().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);

        dailyLedger = new DailyLedger("test", legalEntity);
        dailyLedger = dailyLedgerDAO.save(dailyLedger);
        legalEntity.getDailyLedgers().add(dailyLedger);
        Session.get().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);

        LegalEntityAccess legalEntityAccess = new LegalEntityAccess(appUser, legalEntity);
        legalEntityAccess = legalEntityAccessDAO.save(legalEntityAccess);

        //TODO login wicket user
//        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
//        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "test", simpleGrantedAuthorities));

    }


}
