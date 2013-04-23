package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.ContactPage;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author
 */
@Ignore
public class TestContactPage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(ContactPage.class);
        tester.assertRenderedPage(ContactPage.class);

    }
}
