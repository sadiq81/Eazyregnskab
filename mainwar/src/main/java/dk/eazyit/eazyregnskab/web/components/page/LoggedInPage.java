package dk.eazyit.eazyregnskab.web.components.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.DailyLedgerService;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.session.SessionAware;
import dk.eazyit.eazyregnskab.web.components.choice.LegalEntityChooser;
import dk.eazyit.eazyregnskab.web.components.navigation.LinkList;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuSetup;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author EazyIT
 */
@RequireHttps
public abstract class LoggedInPage extends AppBasePage implements SessionAware {

    public static final String PARAM_LEGAL_ENTITY = "legalEntity";

    @SpringBean
    protected LoginService loginService;

    @SpringBean
    protected LegalEntityService legalEntityService;

    @SpringBean
    protected FinanceAccountService financeAccountService;

    @SpringBean
    protected DailyLedgerService dailyLedgerService;

    LegalEntityChooser legalEntityChooser;

    static final Logger LOG = LoggerFactory.getLogger(LoggedInPage.class);
    protected NotificationPanel feedbackPanel;

    public LoggedInPage() {
        super();
    }

    public LoggedInPage(IModel<?> model) {
        super(model);
    }

    public LoggedInPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage(PageParameters parameters) {

        setOutputMarkupPlaceholderTag(true);
        add(feedbackPanel = new NotificationPanel("feedback"));
        feedbackPanel.setOutputMarkupPlaceholderTag(true);
        ensureUserInfo(parameters);

        add(new LinkList("linkList", MenuSetup.createSubMenuList(this.getClass().getAnnotation(MenuPosition.class).parentPage())));

        add(legalEntityChooser = new LegalEntityChooser("legalEntityChooser"));

    }

    protected void ensureUserInfo(PageParameters parameters) {

        Session session = getSession();

        AppUser currentUser = getCurrentUser();
        if (currentUser == null) {
            currentUser = getAppUser();
            LOG.debug("Setting CurrentUser");
            session.setAttribute(AppUser.ATTRIBUTE_NAME, currentUser);
        }

        LegalEntity currentLegalEntity = getCurrentLegalEntity();
        if (currentLegalEntity == null) {
            currentLegalEntity = legalEntityService.findLegalEntityByUser(currentUser).get(0);
            LOG.debug("Setting CurrentLegalEntity");
            session.setAttribute(LegalEntity.ATTRIBUTE_NAME, currentLegalEntity);
        }

        DailyLedger currentDailyLedger = getCurrentDailyLedger();
        if (currentDailyLedger == null) {
            currentDailyLedger = dailyLedgerService.findDailyLedgerByLegalEntity(currentLegalEntity).get(0);
            LOG.debug("Setting CurrentDailyLedger");
            session.setAttribute(DailyLedger.ATTRIBUTE_NAME, currentDailyLedger);
        }

        if (currentUser == null || currentLegalEntity == null || currentDailyLedger == null) {
            LOG.warn("EnsureUserInfo Failed with AppUser: " + currentUser + " LegalEntity: " + currentLegalEntity + " DailyLedger: " + currentDailyLedger);
            throw new NullPointerException("EnsureUserInfo Failed");
        }
    }

    public void changeLegalEntity() {
        addOrReplace(legalEntityChooser, legalEntityChooser = new LegalEntityChooser("legalEntityChooser"));
    }

    private AppUser getAppUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return loginService.findAppUserByUsername(name);
    }

    public AppUser getCurrentUser() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentUser();
    }

    public LegalEntity getCurrentLegalEntity() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentLegalEntity();
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        ((EazyregnskabSesssion) Session.get()).setCurrentLegalEntity(legalEntity);
    }

    public DailyLedger getCurrentDailyLedger() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentDailyLedger();
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        ((EazyregnskabSesssion) Session.get()).setCurrentDailyLedger(dailyLedger);
    }


    protected abstract void configureComponents();
}