package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.PricingPage;
import org.junit.Test;

/**
 * @author
 */
public class PricingPagePage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(PricingPage.class);
        tester.assertRenderedPage(PricingPage.class);

    }
}
