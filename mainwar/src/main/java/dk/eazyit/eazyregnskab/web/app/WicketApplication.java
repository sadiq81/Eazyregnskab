package dk.eazyit.eazyregnskab.web.app;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.settings.BootstrapSettings;
import dk.eazyit.eazyregnskab.web.app.front.HomePage;
import dk.eazyit.eazyregnskab.web.app.secure.bookkeeping.BookkeepingPage;
import dk.eazyit.eazyregnskab.web.app.secure.reports.BalancePage;
import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 *
 * @see dk.eazyit.Start#main(String[])
 */
@Component
public class WicketApplication extends WebApplication {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */

    ApplicationContext ctx;

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        getApplicationSettings().setAccessDeniedPage(HomePage.class);

        Bootstrap.install(Application.get(), new BootstrapSettings());
        mountPackage("/app", HomePage.class);
        mountPackage("app/secure/bookkeeping", BookkeepingPage.class);
        mountPackage("app/secure/settings", BaseDataPage.class);
        mountPackage("app/secure/reports", BalancePage.class);

        initSpring();


        // add your configuration here
    }

    protected void initSpring() {
        // Initialize Spring Dependency Injection
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }


}
