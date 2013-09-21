package dk.eazyit.eazyregnskab.web.app;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.web.app.front.ErrorPage;
import dk.eazyit.eazyregnskab.web.app.front.HomePage;
import dk.eazyit.eazyregnskab.web.app.front.LoginPage;
import dk.eazyit.eazyregnskab.web.app.front.NotFoundPage;
import dk.eazyit.eazyregnskab.web.app.secure.bookkeeping.BookkeepingPage;
import dk.eazyit.eazyregnskab.web.app.secure.reports.BalancePage;
import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
import dk.eazyit.eazyregnskab.web.components.converter.EazyConverterLocator;
import dk.eazyit.eazyregnskab.web.components.markupfilter.HeaderHandler;
import org.apache.wicket.Application;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Locale;

/**
 *
 */
@Component
public class WicketApplication extends AuthenticatedWebApplication implements ApplicationContextAware {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */

    ApplicationContext ctx;
    private static final Logger LOG = LoggerFactory.getLogger(WicketApplication.class);

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

        getMarkupSettings().setMarkupFactory(
                new MarkupFactory() {
                    @Override
                    public MarkupParser newMarkupParser(MarkupResourceStream resource) {
                        MarkupParser parser = super.newMarkupParser(resource);
                        parser.add(new HeaderHandler());
                        return parser;
                    }

                    ;
                });

        LOG.info("Starting application on time " + Calendar.getInstance());
        Locale.setDefault(Locale.ROOT);

//        getDebugSettings().setAjaxDebugModeEnabled(true);

        getApplicationSettings().setAccessDeniedPage(HomePage.class);
        getApplicationSettings().setPageExpiredErrorPage(LoginPage.class);
        getApplicationSettings().setInternalErrorPage(ErrorPage.class);

        Bootstrap.install(Application.get(), new BootstrapSettings());
        mountPackage("/app/front", HomePage.class);
        mountPackage("app/secure/bookkeeping", BookkeepingPage.class);
        mountPackage("app/secure/settings", BaseDataPage.class);
        mountPackage("app/secure/reports", BalancePage.class);
        mountPage("/404", NotFoundPage.class);

        getComponentInstantiationListeners().add(new SpringComponentInjector(this, ctx, true));

        if (System.getProperty("local") != null) {
            setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig(8080, 8443)));
        } else {
            setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig(80, 443)));
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return EazyregnskabSesssion.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        return new EazyConverterLocator();
    }
}