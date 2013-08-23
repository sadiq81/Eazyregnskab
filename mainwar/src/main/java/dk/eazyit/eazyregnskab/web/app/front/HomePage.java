package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.carousel.LinkCarousel;
import dk.eazyit.eazyregnskab.web.components.carousel.LinkCarouselImage;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HomePage extends AppBasePage {
    private static final long serialVersionUID = 1L;

    private static final List<String> allowedLanguages = new ArrayList<String>(Arrays.asList("en", "da"));

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

        String language = allowedLanguages.contains(getSession().getLocale().getLanguage()) ? "_" + getSession().getLocale().getLanguage() : "";
        List<LinkCarouselImage> list = new LinkedList<LinkCarouselImage>();
        list.add(new LinkCarouselImage("carousel.eazy.booking", "carousel.eazy.booking.explanation", "/pictures/carousel/bookkeeping" + language + ".png", "http://www.eazyregnskab.dk/app/front/SignUpPage"));
        list.add(new LinkCarouselImage("carousel.unlimited.units", "carousel.unlimited.units.explanation", "/pictures/carousel/legal_entities" + language + ".png", "http://www.eazyregnskab.dk/app/front/SignUpPage"));
        list.add(new LinkCarouselImage("carousel.reports", "carousel.reports.explanation", "/pictures/carousel/reports" + language + ".png", "http://www.eazyregnskab.dk/app/front/SignUpPage"));
        add(new LinkCarousel("carousel", list));

        add(new ContextImage("logo", "/pictures/logo/Logo470px.png"));

        add(new BookmarkablePageLink("signUp", SignUpPage.class));
        add(new BookmarkablePageLink("whatYouGet", WhatYouGetPage.class));
        add(new BookmarkablePageLink("forAdministrators", ForAdministratorsPage.class));
        add(new BookmarkablePageLink("pricing", PricingPage.class));

    }
}
