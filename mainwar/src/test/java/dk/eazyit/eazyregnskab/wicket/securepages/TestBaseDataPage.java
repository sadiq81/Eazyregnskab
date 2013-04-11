package dk.eazyit.eazyregnskab.wicket.securepages;

import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
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
