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
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
@RequireHttps
@AuthorizeInstantiation("USER")
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

        if (getCurrentUser() == null || getCurrentLegalEntity() == null || getCurrentDailyLedger() == null) {
            LOG.warn("EnsureUserInfo Failed");
            throw new NullPointerException("EnsureUserInfo Failed");
        }
    }

    public void changeLegalEntity() {
        addOrReplace(legalEntityChooser, legalEntityChooser = new LegalEntityChooser("legalEntityChooser"));
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