package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.markup.html.link.Link;
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
    protected void addToPage() {

        add(new Link<Void>("signUp") {
            @Override
            public void onClick() {
                setResponsePage(SignUpPage.class);
            }
        });
        add(new Link<Void>("whatYouGet") {
            @Override
            public void onClick() {
                setResponsePage(WhatYouGetPage.class);
            }
        });
        add(new Link<Void>("forAdministrators") {
            @Override
            public void onClick() {
                setResponsePage(ForAdministratorsPage.class);
            }
        });
        add(new Link<Void>("pricing") {
            @Override
            public void onClick() {
                setResponsePage(PricingPage.class);
            }
        });
    }
}
