package dk.eazyit.eazyregnskab.wicket;

import dk.eazyit.eazyregnskab.web.app.WicketApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;

/**
 * Created with IntelliJ IDEA.
 * User: tommysadiqhinrichsen
 * Date: 12/12/12
 * Time: 14.52
 * To change this template use File | Settings | File Templates.
 */
public class MockWicketApplication extends WicketApplication {

    private ApplicationContextMock mockContext;

    @Override
    protected void initSpring() {

        mockContext = new ApplicationContextMock();

        getComponentInstantiationListeners().add(new SpringComponentInjector(this, mockContext, true));
    }

    public ApplicationContextMock getMockContext() {
        return mockContext;
    }


}
