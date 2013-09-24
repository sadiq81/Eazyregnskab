package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.SimpleAppUserInfo;
import dk.eazyit.eazyregnskab.web.app.secure.bookkeeping.BookkeepingPage;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authentication.IAuthenticationStrategy;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class SignInForm extends StatelessForm<SimpleAppUserInfo> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(SignInForm.class);

    /**
     * Constructor.
     *
     * @param id id of the form component
     */
    public SignInForm(final String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());

        setModel(new CompoundPropertyModel<SimpleAppUserInfo>(new SimpleAppUserInfo()));

        // Attach textfields for username and password
        add(new TextField<String>("username"));
        add(new PasswordTextField("password"));
        add(new CheckBox("rememberMe"));
    }


    /**
     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
     */
    @Override
    public final void onSubmit() {

        LOG.debug("Signin attempt " + getModelObject().getUsername());
        IAuthenticationStrategy strategy = getApplication().getSecuritySettings().getAuthenticationStrategy();

        if (signIn(getModelObject().getUsername(), getModelObject().getPassword())) {
            if (getModelObject().isRememberMe() == true) {
                strategy.save(getModelObject().getUsername(), getModelObject().getPassword());
                LOG.debug("Signin attempt success, saved cookie " + getModelObject().getUsername());
            } else {
                strategy.remove();
                LOG.debug("Signin attempt success, not saving cookie " + getModelObject().getUsername());
            }
            onSignInSucceeded();
        } else {
            LOG.debug("Signin attempt failure, deleted cookie " + getModelObject().getUsername());
            onSignInFailed();
            strategy.remove();
        }
    }

    /**
     * Sign in user if possible.
     *
     * @param username The username
     * @param password The password
     * @return True if signin was successful
     */
    private boolean signIn(String username, String password) {
        return AuthenticatedWebSession.get().signIn(username, password);
    }

    /**
     * @return true, if signed in
     */
    private boolean isSignedIn() {
        return AuthenticatedWebSession.get().isSignedIn();
    }

    /**
     * Called when sign in failed
     */
    protected void onSignInFailed() {
        getSession().error(new NotificationMessage(new ResourceModel("error.on.login")).hideAfter(Duration.seconds(5)));
    }

    /**
     * Called when sign in was successful
     */
    protected void onSignInSucceeded() {
        // If login has been called because the user was not yet logged in, than continue to the
        // original destination, otherwise to the Home page
        continueToOriginalDestination();
        setResponsePage(BookkeepingPage.class);
    }

    @Override
    protected void onBeforeRender() {
        // logged in already?
        if (isSignedIn() == false) {
            IAuthenticationStrategy authenticationStrategy = getApplication().getSecuritySettings()
                    .getAuthenticationStrategy();
            // get username and password from persistence store
            String[] data = authenticationStrategy.load();

            if ((data != null) && (data.length > 1)) {
                // try to sign in the user
                if (signIn(data[0], data[1])) {
                    // logon successful. Continue to the original destination
                    LOG.debug("User allready signed in " + data[0]);
                    continueToOriginalDestination();
                    // Ups, no original destination. Go to the home page
                    throw new RestartResponseException(BookkeepingPage.class);
                } else {
                    // the loaded credentials are wrong. erase them.
                    authenticationStrategy.remove();
                }
            }
        } else {
            throw new RestartResponseException(BookkeepingPage.class);
        }


        // don't forget
        super.onBeforeRender();
    }
}