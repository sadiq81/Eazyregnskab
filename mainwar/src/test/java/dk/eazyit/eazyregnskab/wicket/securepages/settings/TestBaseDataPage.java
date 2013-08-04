package dk.eazyit.eazyregnskab.wicket.securepages.settings;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Test;

/**
 * @author
 */
public class TestBaseDataPage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(BaseDataPage.class);
        tester.assertRenderedPage(BaseDataPage.class);

        tester.assertComponent("feedback", NotificationPanel.class);
    }

}
