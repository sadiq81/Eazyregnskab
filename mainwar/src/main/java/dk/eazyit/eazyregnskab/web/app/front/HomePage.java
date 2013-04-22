package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage extends AppBasePage {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(HomePage.class);

    public HomePage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public HomePage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public HomePage(final PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }


    @Override
    protected void addToPage(PageParameters parameters) {

        add(new BookmarkablePageLink("signUp",SignUpPage.class));
        add(new BookmarkablePageLink("whatYouGet",WhatYouGetPage.class));
        add(new BookmarkablePageLink("forAdministrators",ForAdministratorsPage.class));
        add(new BookmarkablePageLink("pricing",PricingPage.class));

    }
}
