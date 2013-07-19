package dk.eazyit.eazyregnskab.wicket.unsecurepages;

import dk.eazyit.eazyregnskab.TestBaseCase;
import dk.eazyit.eazyregnskab.web.app.front.WhatYouGetPage;
import org.junit.Test;

/**
 * @author
 */
public class TestWhatYouGetPage extends TestBaseCase {

    @Test
    public void testPageRendering() {

        tester.startPage(WhatYouGetPage.class);
        tester.assertRenderedPage(WhatYouGetPage.class);

    }
}
