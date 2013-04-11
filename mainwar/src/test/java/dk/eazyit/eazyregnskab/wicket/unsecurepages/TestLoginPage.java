package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.web.app.front.LoginPage;
import dk.eazyit.eazyregnskab.wicket.TestBaseWicketCase;
import org.junit.Test;

/**
 * @author
 */
public class TestLoginPage extends TestBaseWicketCase {

    @Test
    public void testPageRendering() {

        tester.startPage(LoginPage.class);
        tester.assertRenderedPage(LoginPage.class);

        tester.assertComponent("feedback", NotificationPanel.class);
    }
}
