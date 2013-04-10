package dk.eazyit.eazyregnskab.web.components.page;

import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import dk.eazyit.eazyregnskab.session.CurrentUser;
import dk.eazyit.eazyregnskab.web.components.models.AppUserModel;
import dk.eazyit.eazyregnskab.web.components.models.LegalEntityModel;
import dk.eazyit.eazyregnskab.web.components.navigation.LinkList;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuSetup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author EazyIT
 */
public class LoggedInPage extends AppBasePage {

    public static final String PARAM_LEGAL_ENTITY = "legalEntity";

    @SpringBean
    LoginService loginService;

    @SpringBean
    LegalEntityService legalEntityService;

    DropDownChoice<LegalEntity> legalEntityDropDownChoice;
    LegalEntityModel legalEntityModel;

    private Log logger;
    protected FeedbackPanel feedbackPanel;

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
        logger = LogFactory.getLog(this.getClass());
        setOutputMarkupPlaceholderTag(true);
        add(feedbackPanel = new FeedbackPanel("feedback"));
        ensureUserInfo(parameters);

        add(new LinkList("linkList", MenuSetup.createSideMenuList(this.getClass().getAnnotation(MenuPosition.class).parentPage())));

        add(legalEntityDropDownChoice = new DropDownChoice<LegalEntity>("legalEntityList",
                legalEntityModel = getSelectedLegalEntity().getLegalEntityModel(),
                legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()),
                new ChoiceRenderer<LegalEntity>("name", "id")));

        legalEntityDropDownChoice.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getPage());
            }
        }));
        legalEntityDropDownChoice.setOutputMarkupPlaceholderTag(true);

    }

    private void ensureUserInfo(PageParameters parameters) {

        Session session = getSession();

        CurrentUser currentUser = getCurrentUser();
        if (currentUser == null) {
            logger.debug("Creating CurrentUser");
            currentUser = new CurrentUser();
            session.setAttribute(CurrentUser.ATTRIBUTE_NAME, currentUser);
        }
        if (currentUser.getAppUserModel() == null) {
            logger.debug("No user set");
            currentUser.setAppUserModel(getAppUserModel());
            logger.debug("set user " + currentUser.getAppUserModel().getObject().getUsername());
        }

        CurrentLegalEntity currentLegalEntity = getSelectedLegalEntity();
        if (currentLegalEntity == null) {
            logger.debug("Creating CurrentLegalEntity");
            currentLegalEntity = new CurrentLegalEntity();
            session.setAttribute(CurrentLegalEntity.ATTRIBUTE_NAME, currentLegalEntity);
        }
        if (currentLegalEntity.getLegalEntityModel() == null) {
            logger.debug("No legalEntity selected");
            //TODO will fail if no legal entities
            currentLegalEntity.setLegalEntityModel(new LegalEntityModel(legalEntityService.findLegalEntityByUser(currentUser.getAppUserModel().getObject()).get(0)));
            logger.debug("Selected legalEntity as first in user access");
        }

    }

    private AppUserModel getAppUserModel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return new AppUserModel(loginService.findAppUserByUsername(name));
    }

    protected CurrentUser getCurrentUser() {
        return (CurrentUser) getSession().getAttribute(CurrentUser.ATTRIBUTE_NAME);
    }

    protected CurrentLegalEntity getSelectedLegalEntity() {
        return (CurrentLegalEntity) getSession().getAttribute(CurrentLegalEntity.ATTRIBUTE_NAME);
    }

    protected void updateSelections() {
        DropDownChoice<LegalEntity> temp = new DropDownChoice<LegalEntity>("legalEntityList",
                legalEntityModel = getSelectedLegalEntity().getLegalEntityModel(),
                legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()),
                new ChoiceRenderer<LegalEntity>("name", "id"));
        temp.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getPage());
            }
        }));
        temp.setOutputMarkupPlaceholderTag(true);
        addOrReplace(legalEntityDropDownChoice, temp);
        temp.setParent(this);
        legalEntityDropDownChoice.setParent(this);
        legalEntityDropDownChoice = temp;
    }
}