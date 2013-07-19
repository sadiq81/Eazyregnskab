package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.ForAdministratorsPage;
import org.junit.Test;

/**
 * @author
 */
public class ForAdministratorsPagePage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(ForAdministratorsPage.class);
        tester.assertRenderedPage(ForAdministratorsPage.class);

    }
}
