package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.AboutPage;
import org.junit.Test;

/**
 * @author
 */
public class AboutPagePage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(AboutPage.class);
        tester.assertRenderedPage(AboutPage.class);

    }
}
