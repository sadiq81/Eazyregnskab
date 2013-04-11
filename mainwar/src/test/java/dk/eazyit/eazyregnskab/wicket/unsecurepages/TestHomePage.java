package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.web.app.front.HomePage;
import dk.eazyit.eazyregnskab.web.app.front.SignUpPage;
import dk.eazyit.eazyregnskab.wicket.TestBaseWicketCase;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

/**
 * @author
 */
public class TestHomePage extends TestBaseWicketCase {

    @Test
    public void testPageRendering() {

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);

        tester.assertBookmarkablePageLink("signUp", SignUpPage.class, new PageParameters());
        tester.assertBookmarkablePageLink("whatYouGet", SignUpPage.class, new PageParameters());
        tester.assertBookmarkablePageLink("forAdministrators", SignUpPage.class, new PageParameters());
        tester.assertBookmarkablePageLink("pricing", SignUpPage.class, new PageParameters());

    }
}
