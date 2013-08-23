package dk.eazyit.eazyregnskab.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author
 */
public class Start {

    public static void main(String[] arg) throws Exception {
        Server server = new Server();

        SelectChannelConnector http = new SelectChannelConnector();
        http.setPort(isLocal() ? 8080 : 80);
        http.setConfidentialPort(isLocal() ? 8443 : 443);

        Resource keystore = Resource.newClassPathResource("www.eazyregnskab.dk.keystore");
        if (keystore == null) throw new NullPointerException("keystore not found");
        SslContextFactory factory = new SslContextFactory();
        factory.setKeyStoreResource(keystore);
        factory.setKeyStorePassword("Ostekage83");
        factory.setTrustStoreResource(keystore);
        factory.setKeyManagerPassword("Ostekage83");
        SslSocketConnector https = new SslSocketConnector(factory);
        https.setPort(isLocal() ? 8443 : 443);
        https.setAcceptors(4);

        server.setConnectors(new Connector[]{http, https});

        WebAppContext webappcontext = new WebAppContext();
        webappcontext.setContextPath("/");
        webappcontext.setWar(isLocal() ? "mainwar/target/eazyregnskab.war" : "eazyregnskab.war");
        webappcontext.setParentLoaderPriority(true);

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{webappcontext});

        server.setHandler(handlers);

        EazyRegnskabLoginService loginService = new EazyRegnskabLoginService();
        loginService.setConfig(isLocal() ? "jetty/src/main/resources/jetty-realm.properties" : "jetty-realm.properties");
        loginService.setName("Monitoring");
        server.addBean(loginService);

//        server.setDumpAfterStart(true);
        server.setGracefulShutdown(1000);
        server.setStopAtShutdown(true);

        server.start();
        server.join();
    }

    private static boolean isLocal() {
        return System.getProperty("local") != null;

    }

    private static boolean isDevelopment() {
        return System.getProperty("development") != null;
    }

}
