package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.web.app.front.AboutPage;
import dk.eazyit.eazyregnskab.wicket.TestBaseWicketCase;
import org.junit.Test;

/**
 * @author
 */
public class TestAboutPage extends TestBaseWicketCase {

    @Test
    public void testPageRendering() {

        tester.startPage(AboutPage.class);
        tester.assertRenderedPage(AboutPage.class);

    }
}
