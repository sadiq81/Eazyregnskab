package dk.eazyit.eazyregnskab.web.app;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ContactPage extends AppBasePage {
    private static final long serialVersionUID = 1L;

    public ContactPage() {
        super();
    }

    public ContactPage(IModel<?> model) {
        super(model);
    }

    public ContactPage(final PageParameters parameters) {
        super(parameters);

    }
}
