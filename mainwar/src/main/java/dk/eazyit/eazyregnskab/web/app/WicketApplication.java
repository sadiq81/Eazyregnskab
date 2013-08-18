package dk.eazyit.eazyregnskab.web.app;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.web.app.front.HomePage;
import dk.eazyit.eazyregnskab.web.app.secure.bookkeeping.BookkeepingPage;
import dk.eazyit.eazyregnskab.web.app.secure.reports.BalancePage;
import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
import dk.eazyit.eazyregnskab.web.components.markupfilter.HeaderHandler;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
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
public class WicketApplication extends WebApplication implements ApplicationContextAware {
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

        Bootstrap.install(Application.get(), new BootstrapSettings());
        mountPackage("/app/front", HomePage.class);
        mountPackage("app/secure/bookkeeping", BookkeepingPage.class);
        mountPackage("app/secure/settings", BaseDataPage.class);
        mountPackage("app/secure/reports", BalancePage.class);

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
    public Session newSession(Request request, Response response) {
        return new EazyregnskabSesssion(request);
    }


}