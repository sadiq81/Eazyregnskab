package dk.eazyit.eazyregnskab.web.app.front;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.domain.ConfirmAppUserInfo;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderPasswordField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class ConfirmPage extends AppBasePage {

    @SpringBean(name = "loginService")
    private LoginService loginService;

    ConfirmAppUserInfo confirmAppUserInfo;

    SignUpForm signUpForm;
    BookmarkablePageLink login;

    final Logger LOG = LoggerFactory.getLogger(ConfirmPage.class);

    public ConfirmPage() {
    }

    public ConfirmPage(IModel<?> model) {
        super(model);
    }

    public ConfirmPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage(PageParameters pageParameters) {

        add(new NotificationPanel("feedback").setOutputMarkupPlaceholderTag(true));

        add(signUpForm = (SignUpForm) new SignUpForm("confirm_account", new CompoundPropertyModel<ConfirmAppUserInfo>(confirmAppUserInfo = new ConfirmAppUserInfo(pageParameters.get("id").toString()))).setOutputMarkupId(true));

        add(login = (BookmarkablePageLink) ((BookmarkablePageLink)new BookmarkablePageLink<LoginPage>("loginLink", LoginPage.class).setOutputMarkupPlaceholderTag(true).setVisibilityAllowed(false)).add(new Label("login.label", new ResourceModel("login.link.text"))));

    }

    public final class SignUpForm extends Form<ConfirmAppUserInfo> {

        public SignUpForm(final String id, IModel model) {
            super(id, model);
            add(new PlaceholderTextField<String>("username").setRequired(true));
            add(new PlaceholderPasswordField("password").setRequired(true));
            add(new AjaxButton("save") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    super.onSubmit(target, form);
                    ConfirmAppUserInfo userinfo = signUpForm.getModelObject();

                    if (loginService.activeUser(userinfo.getUsername(), userinfo.getPassword(), userinfo.getUUID())) {
                        LOG.info("Confirmed account for " + userinfo.getUsername());
                        getSession().info(getString("confirm.account.for.user") + " " + userinfo.getUsername());
                        target.add(signUpForm.setVisibilityAllowed(false), login.setVisibilityAllowed(true));
                    } else {
                        LOG.info("Unsuccessful confirmation of account for " + userinfo.getUsername());
                        getSession().error(getString("failure.to.confirm.account.for.user") + " " + userinfo.getUsername());
                    }
                    target.add(getPage().get("feedback"));
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    super.onError(target, form);
                    target.add(getPage().get("feedback"));
                }
            }.add(new Label("save.label", new ResourceModel("confirm.account"))));
        }
    }
}
