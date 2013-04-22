package dk.eazyit.eazyregnskab.wicket.securepages.reports;

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

    }

}
