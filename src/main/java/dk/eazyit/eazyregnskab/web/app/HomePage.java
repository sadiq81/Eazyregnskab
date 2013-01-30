package dk.eazyit.eazyregnskab.web.app;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends AppBasePage {
    private static final long serialVersionUID = 1L;

    public HomePage() {
        super();
    }

    public HomePage(IModel<?> model) {
        super(model);
    }

    public HomePage(final PageParameters parameters) {
        super(parameters);

    }
}
