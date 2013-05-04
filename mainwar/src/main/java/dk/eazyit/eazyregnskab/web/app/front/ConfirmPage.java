package dk.eazyit.eazyregnskab.web.app.front;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.domain.ConfirmAppUserInfo;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderPasswordField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
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

        add(new NotificationPanel("feedback"));

        SignUpForm form = new SignUpForm("confirm_account",
                new CompoundPropertyModel<ConfirmAppUserInfo>(confirmAppUserInfo = new ConfirmAppUserInfo(pageParameters.get("id").toString())));
        add(form);

    }

    public final class SignUpForm extends Form<ConfirmAppUserInfo> {

        public SignUpForm(final String id, IModel model) {
            super(id, model);
            add(new PlaceholderTextField<String>("username"));
            add(new PlaceholderPasswordField("password"));
        }

        @Override
        public final void onSubmit() {
            ConfirmAppUserInfo userinfo = getModelObject();
            if (loginService.activeUser(userinfo.getUsername(), userinfo.getPassword(), userinfo.getUUID())) {
                LOG.info("Confirmed account for " + userinfo.getUsername());
                getSession().info(getString("confirm.account.for.user") + " " + userinfo.getUsername());
            } else {
                LOG.info("Unsuccessful confirmation of account for " + userinfo.getUsername());
                getSession().error(getString("failure.to.confirm.account.for.user") + " " + userinfo.getUsername());
            }
        }

        @Override
        protected void onConfigure() {
            super.onConfigure();

            setVisibilityAllowed(true);
        }
    }
}
