package dk.eazyit.eazyregnskab.wicket.securepages.bookkeeping;

import dk.eazyit.eazyregnskab.web.app.secure.bookkeeping.BookkeepingPage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Test;

/**
 * @author
 */
public class TestBookkeepingPage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(BookkeepingPage.class);
        tester.assertRenderedPage(BookkeepingPage.class);

    }

}
