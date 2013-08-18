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

/**
 * @author
 */
public class SignInForm extends StatelessForm<SimpleAppUserInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param id id of the form component
     */
    public SignInForm(final String id) {
        super(id);

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

        IAuthenticationStrategy strategy = getApplication().getSecuritySettings().getAuthenticationStrategy();

        if (signIn(getModelObject().getUsername(), getModelObject().getPassword())) {
            if (getModelObject().isRememberMe() == true) {
                strategy.save(getModelObject().getUsername(), getModelObject().getPassword());
            } else {
                strategy.remove();
            }
            onSignInSucceeded();
        } else {
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
                    continueToOriginalDestination();
                    // Ups, no original destination. Go to the home page
                    throw new RestartResponseException(BookkeepingPage.class);
                } else {
                    // the loaded credentials are wrong. erase them.
                    authenticationStrategy.remove();
                }
            }
        } else{
            throw new RestartResponseException(BookkeepingPage.class);
        }


        // don't forget
        super.onBeforeRender();
    }
}