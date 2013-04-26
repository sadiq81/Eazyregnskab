package dk.eazyit.eazyregnskab.web.components.page;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.session.CurrentDailyLedger;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import dk.eazyit.eazyregnskab.session.CurrentUser;
import dk.eazyit.eazyregnskab.web.components.choice.LegalEntityDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.models.entities.AppUserModel;
import dk.eazyit.eazyregnskab.web.components.models.entities.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.models.entities.LegalEntityModel;
import dk.eazyit.eazyregnskab.web.components.navigation.LinkList;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuSetup;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author EazyIT
 */
public class LoggedInPage extends AppBasePage {

    public static final String PARAM_LEGAL_ENTITY = "legalEntity";

    @SpringBean
    protected LoginService loginService;

    @SpringBean
    protected LegalEntityService legalEntityService;

    @SpringBean
    protected FinanceAccountService financeAccountService;

    LegalEntityDropDownChoice legalEntityDropDownChoice;

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

        add(legalEntityDropDownChoice = new LegalEntityDropDownChoice("legalEntityList", this));


    }

    protected void ensureUserInfo(PageParameters parameters) {

        Session session = getSession();

        CurrentUser currentUser = getCurrentUser();
        if (currentUser == null) {
            LOG.debug("Creating CurrentUser");
            currentUser = new CurrentUser();
            session.setAttribute(CurrentUser.ATTRIBUTE_NAME, currentUser);
        }
        if (currentUser.getAppUserModel() == null) {
            LOG.debug("No user set");
            currentUser.setAppUserModel(getAppUserModel());
            LOG.debug("set user " + currentUser.getAppUserModel().getObject().getUsername());
        }

        CurrentLegalEntity currentLegalEntity = getSelectedLegalEntity();
        if (currentLegalEntity == null) {
            LOG.debug("Creating CurrentLegalEntity");
            currentLegalEntity = new CurrentLegalEntity();
            session.setAttribute(CurrentLegalEntity.ATTRIBUTE_NAME, currentLegalEntity);
        }
        if (currentLegalEntity.getLegalEntityModel() == null) {
            LOG.debug("No legalEntity selected");
            //TODO will fail if no legal entities
            currentLegalEntity.setLegalEntityModel(new LegalEntityModel(legalEntityService.findLegalEntityByUser(currentUser.getAppUserModel().getObject()).get(0)));
            LOG.debug("Selected legalEntity as first in user access");
        }
        CurrentDailyLedger currentDailyLedger = getCurrentDailyLedger();
        if (currentDailyLedger == null) {
            LOG.debug("Creating CurrentDailyLedger");
            currentDailyLedger = new CurrentDailyLedger();
            session.setAttribute(CurrentDailyLedger.ATTRIBUTE_NAME, currentDailyLedger);
        }
        if (currentDailyLedger.getDailyLedgerModel() == null) {
            //TODO will fail if no daily ledger
            LOG.debug("No dailyLedger selected");
            currentDailyLedger.setDailyLedgerModel(new DailyLedgerModel(financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()).get(0)));
            LOG.debug("Selected daily ledger as first in legal entity");
        }
    }

    private AppUserModel getAppUserModel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return new AppUserModel(loginService.findAppUserByUsername(name));
    }

    public CurrentUser getCurrentUser() {
        return (CurrentUser) getSession().getAttribute(CurrentUser.ATTRIBUTE_NAME);
    }

    public CurrentLegalEntity getSelectedLegalEntity() {
        return (CurrentLegalEntity) getSession().getAttribute(CurrentLegalEntity.ATTRIBUTE_NAME);
    }

    public CurrentDailyLedger getCurrentDailyLedger() {
        return (CurrentDailyLedger) getSession().getAttribute(CurrentDailyLedger.ATTRIBUTE_NAME);
    }

    public void updateLegalEntitySelections() {
        LOG.debug("Updating legal entity selections");
        addOrReplace(legalEntityDropDownChoice, new LegalEntityDropDownChoice("legalEntityList", this));
    }

    public void changedLegalEntity(AjaxRequestTarget target) {
        getCurrentDailyLedger().getDailyLedgerModel().setObject(financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()).get(0));
    }

}