package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.security.PasswordEncoder;
import dk.eazyit.eazyregnskab.services.DailyLedgerService;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private LegalEntityService legalEntityService;

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
        } else if (PasswordEncoder.getInstance().encode(password, username).equals(appUser.getPassword())) {
            log.info("appUser logged in: " + username + "from " + ((ServletWebRequest) RequestCycle.get().getRequest()).getContainerRequest().getRemoteHost());
            log.debug("Setting Current User");
            setCurrentUser(appUser);

            LegalEntity currentLegalEntity = legalEntityService.findLegalEntityByUser(getCurrentUser()).get(0);
            log.debug("Setting CurrentLegalEntity");
            setAttribute(LegalEntity.ATTRIBUTE_NAME, currentLegalEntity);

            DailyLedger currentDailyLedger = dailyLedgerService.findDailyLedgerByLegalEntity(currentLegalEntity).get(0);
            log.debug("Setting CurrentDailyLedger");
            setAttribute(DailyLedger.ATTRIBUTE_NAME, currentDailyLedger);

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
        if (getCurrentUser() != null) {
            log.info("appUser logged out: " + getCurrentUser().getUsername() + " from " + ((ServletWebRequest) RequestCycle.get().getRequest()).getContainerRequest().getRemoteHost());
        }
        getApplication().getSecuritySettings().getAuthenticationStrategy().remove();
        Session.get().removeAttribute(AppUser.ATTRIBUTE_NAME);
        Session.get().removeAttribute(LegalEntity.ATTRIBUTE_NAME);
        Session.get().removeAttribute(DailyLedger.ATTRIBUTE_NAME);
        super.invalidate();
    }
}
