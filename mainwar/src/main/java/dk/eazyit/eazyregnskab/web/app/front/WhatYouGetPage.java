package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.page.InfoPage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhatYouGetPage extends InfoPage {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(WhatYouGetPage.class);

    public WhatYouGetPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public WhatYouGetPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public WhatYouGetPage(final PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());

    }

    @Override
    protected void addToPage(PageParameters parameters) {
        ContextImage image = new ContextImage("right_side", "/css/pictures/get_right_side.jpg"){
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("alt","© Stelianion | Stock Free Images & Dreamstime Stock Photos");
            }
        };
        add(image);
    }
}
