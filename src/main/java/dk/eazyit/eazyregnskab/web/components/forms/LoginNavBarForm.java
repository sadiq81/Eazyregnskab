package dk.eazyit.eazyregnskab.web.components.forms;

import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import de.agilecoders.wicket.markup.html.bootstrap.button.TypedAjaxButton;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarForm;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.web.app.secure.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderPasswordField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author EazyIT
 */
public class LoginNavBarForm extends NavbarForm<AppUser> {

    public LoginNavBarForm(String componentId) {
        this(componentId, null);
    }

    public LoginNavBarForm(String componentId, IModel<AppUser> model) {
        super(componentId, model);
        initComponents();
    }

    private void initComponents() {



        PlaceholderTextField<String> username = new PlaceholderTextField<String>("userName", new PropertyModel(getModelObject(), "username"));
        username.setRequired(true);
        username.setPlaceholder(new ResourceModel("username").getObject());
        add(username);

        PlaceholderPasswordField password = new PlaceholderPasswordField("password", new PropertyModel<String>(getModelObject(), "password"));
        password.setRequired(true);
        password.setPlaceholder(new ResourceModel("password").getObject());
        add(password);

        TypedAjaxButton login = new TypedAjaxButton("login", new ResourceModel("login"), ButtonType.Default) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);

            }
        };
        add(login);


    }

    @Override
    public boolean isVisible() {
        return !(getPage() instanceof LoggedInPage); //TODO depend on logged in user
    }
}
