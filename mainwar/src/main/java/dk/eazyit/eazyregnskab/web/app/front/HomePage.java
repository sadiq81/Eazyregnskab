package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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


    @Override
    protected void addToPage(PageParameters parameters) {

        add(new BookmarkablePageLink("signUp",SignUpPage.class));
        add(new BookmarkablePageLink("whatYouGet",SignUpPage.class));
        add(new BookmarkablePageLink("forAdministrators",SignUpPage.class));
        add(new BookmarkablePageLink("pricing",SignUpPage.class));

    }
}
