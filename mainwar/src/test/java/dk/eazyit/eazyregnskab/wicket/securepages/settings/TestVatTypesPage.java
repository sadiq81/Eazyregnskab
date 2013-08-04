package dk.eazyit.eazyregnskab.wicket.securepages.settings;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.web.app.secure.settings.VatTypesPage;
import dk.eazyit.eazyregnskab.wicket.securepages.TestLoggedInPage;
import org.junit.Test;

/**
 * @author
 */
public class TestVatTypesPage extends TestLoggedInPage {

    @Test
    public void testRendering() {

        tester.startPage(VatTypesPage.class);
        tester.assertRenderedPage(VatTypesPage.class);

        tester.assertComponent("feedback", NotificationPanel.class);

    }

}
