package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

/**
 * @author
 */
public class TestHomePage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(HomePage.class);
        tester.assertRenderedPage(HomePage.class);

        tester.assertBookmarkablePageLink("signUp", SignUpPage.class, new PageParameters());
        tester.assertBookmarkablePageLink("whatYouGet", WhatYouGetPage.class, new PageParameters());
        tester.assertBookmarkablePageLink("forAdministrators", ForAdministratorsPage.class, new PageParameters());
        tester.assertBookmarkablePageLink("pricing", PricingPage.class, new PageParameters());

    }
}
