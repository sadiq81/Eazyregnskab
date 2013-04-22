package dk.eazyit.eazyregnskab.web.components.page;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.session.CurrentDailyLedger;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import dk.eazyit.eazyregnskab.session.CurrentUser;
import dk.eazyit.eazyregnskab.web.components.models.AppUserModel;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.models.LegalEntityModel;
import dk.eazyit.eazyregnskab.web.components.navigation.LinkList;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuSetup;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
    protected final static int DURATION = 5;

    @SpringBean
    LoginService loginService;

    @SpringBean
    LegalEntityService legalEntityService;

    @SpringBean
    FinanceAccountService financeAccountService;

    DropDownChoice<LegalEntity> legalEntityDropDownChoice;
    LegalEntityModel legalEntityModel;
    DailyLedgerModel dailyLedgerModel;

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

        add(new LinkList("linkList", MenuSetup.createSideMenuList(this.getClass().getAnnotation(MenuPosition.class).parentPage())));

        add(legalEntityDropDownChoice = new DropDownChoice<LegalEntity>("legalEntityList",
                legalEntityModel = getSelectedLegalEntity().getLegalEntityModel(),
                legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()),
                new ChoiceRenderer<LegalEntity>("name", "id")));

        legalEntityDropDownChoice.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                LOG.debug("Changed legal entity selections");
                changedLegalEntity(target);
                target.add(getPage());
            }
        }));
        legalEntityDropDownChoice.setOutputMarkupPlaceholderTag(true);

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

    protected void updateLegalEntitySelections() {
        LOG.debug("Updating legal entity selections");
        DropDownChoice<LegalEntity> temp = new DropDownChoice<LegalEntity>("legalEntityList",
                legalEntityModel = getSelectedLegalEntity().getLegalEntityModel(),
                legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()),
                new ChoiceRenderer<LegalEntity>("name", "id"));
        temp.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                LOG.debug("Changed legal entity selections");
                changedLegalEntity(target);
                target.add(getPage());
            }
        }));
        temp.setOutputMarkupPlaceholderTag(true);
        addOrReplace(legalEntityDropDownChoice, temp);
        temp.setParent(this);
        legalEntityDropDownChoice.setParent(this);
        legalEntityDropDownChoice = temp;
    }

    protected void changedLegalEntity(AjaxRequestTarget target) {
        getCurrentDailyLedger().getDailyLedgerModel().setObject(financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()).get(0));
    }

}