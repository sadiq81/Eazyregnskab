package dk.eazyit.eazyregnskab.wicket.securepages.settings;

import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author
 */
@Ignore
public class TestBaseDataPage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(BaseDataPage.class);
        tester.assertRenderedPage(BaseDataPage.class);

    }

}
