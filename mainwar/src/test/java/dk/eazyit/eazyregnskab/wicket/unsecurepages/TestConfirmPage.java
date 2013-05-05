package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.ConfirmPage;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author
 */
@Ignore
public class TestConfirmPage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(ConfirmPage.class);
        tester.assertRenderedPage(ConfirmPage.class);

        tester.assertComponent("feedback", NotificationPanel.class);
    }
}
