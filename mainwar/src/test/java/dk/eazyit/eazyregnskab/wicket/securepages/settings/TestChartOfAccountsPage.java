package dk.eazyit.eazyregnskab.wicket.securepages.settings;

import dk.eazyit.eazyregnskab.web.app.secure.settings.ChartOfAccountsPage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author
 */
@Ignore
public class TestChartOfAccountsPage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(ChartOfAccountsPage.class);
        tester.assertRenderedPage(ChartOfAccountsPage.class);

    }

}
