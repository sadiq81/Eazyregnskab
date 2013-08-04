package dk.eazyit.eazyregnskab.wicket.securepages.settings;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.web.app.secure.settings.ChartOfAccountsPage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Test;

/**
 * @author
 */
public class TestChartOfAccountsPage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(ChartOfAccountsPage.class);
        tester.assertRenderedPage(ChartOfAccountsPage.class);

        tester.assertComponent("feedback", NotificationPanel.class);

    }

}
