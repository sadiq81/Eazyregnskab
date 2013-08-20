package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.DailyLedgerService;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LoginService;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * @author
 */
public class EazyregnskabSesssion extends AuthenticatedWebSession {

    @SpringBean
    FinanceAccountService financeAccountService;
    @SpringBean
    DailyLedgerService dailyLedgerService;
    @SpringBean
    private LoginService loginService;
    @SpringBean
    ShaPasswordEncoder shaPasswordEncoder;


    private static final Logger log = LoggerFactory.getLogger(EazyregnskabSesssion.class);

    public EazyregnskabSesssion(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {

        AppUser appUser = loginService.findAppUserByUsername(username);

        if (appUser == null) {
            return false;
        } else if (!appUser.isEnabled()) {
            return false;
        } else if (!appUser.isActive()) {
            return false;
        } else if (shaPasswordEncoder.encodePassword(password, username).equals(appUser.getPassword())) {
            log.info("appUser logged in: " + username);
            setCurrentUser(appUser);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Roles getRoles() {
        return new Roles(loginService.getUserRolesAsString(getCurrentUser()));
    }

    public void setCurrentUser(AppUser appUser) {
        Session.get().setAttribute(AppUser.ATTRIBUTE_NAME, appUser);
    }

    public AppUser getCurrentUser() {
        return (AppUser) Session.get().getAttribute(AppUser.ATTRIBUTE_NAME);
    }

    public LegalEntity getCurrentLegalEntity() {
        return (LegalEntity) Session.get().getAttribute(LegalEntity.ATTRIBUTE_NAME);
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        Session.get().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);
        setCurrentDailyLedger(dailyLedgerService.findDailyLedgerByLegalEntity(legalEntity).get(0));
    }

    public DailyLedger getCurrentDailyLedger() {
        DailyLedger ledger = (DailyLedger) Session.get().getAttribute(DailyLedger.ATTRIBUTE_NAME);
        if (ledger != null && !(ledger.getLegalEntity().equals(getCurrentLegalEntity()))) {
            throw new NullPointerException("Current dailyLedger is not reflecting current LegalEntity");
        }
        return ledger;
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        Session.get().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);
    }

    @Override
    public void invalidate() {
        getApplication().getSecuritySettings().getAuthenticationStrategy().remove();
        Session.get().removeAttribute(AppUser.ATTRIBUTE_NAME);
        Session.get().removeAttribute(LegalEntity.ATTRIBUTE_NAME);
        Session.get().removeAttribute(DailyLedger.ATTRIBUTE_NAME);
        super.invalidate();
    }
}
