package dk.eazyit.eazyregnskab.wicket.securepages.settings;

import dk.eazyit.eazyregnskab.web.app.secure.settings.DailyLedgerPage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Test;

/**
 * @author
 */
public class TestDailyLedgerPage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(DailyLedgerPage.class);
        tester.assertRenderedPage(DailyLedgerPage.class);

    }

}