package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.web.components.page.InfoPage;

/**
 * @author
 */
public class LogoutPage extends InfoPage {

    public LogoutPage() {
        ((EazyregnskabSesssion) getSession()).invalidate();
    }

}
