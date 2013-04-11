package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.web.app.front.ContactPage;
import dk.eazyit.eazyregnskab.wicket.TestBaseWicketCase;
import org.junit.Test;

/**
 * @author
 */
public class TestContactPage extends TestBaseWicketCase {

    @Test
    public void testPageRendering() {

        tester.startPage(ContactPage.class);
        tester.assertRenderedPage(ContactPage.class);

    }
}
