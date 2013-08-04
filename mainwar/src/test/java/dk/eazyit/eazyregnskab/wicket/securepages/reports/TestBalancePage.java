package dk.eazyit.eazyregnskab.wicket.securepages.reports;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.web.app.secure.reports.BalancePage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Test;

/**
 * @author
 */
public class TestBalancePage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(BalancePage.class);
        tester.assertRenderedPage(BalancePage.class);

        tester.assertComponent("feedback", NotificationPanel.class);

    }

}
