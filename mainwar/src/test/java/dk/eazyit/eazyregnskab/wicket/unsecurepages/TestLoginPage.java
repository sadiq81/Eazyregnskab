package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.LoginPage;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author
 */
@Ignore
public class TestLoginPage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(LoginPage.class);
        tester.assertRenderedPage(LoginPage.class);

        tester.assertComponent("feedback", NotificationPanel.class);
    }
}
