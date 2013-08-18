package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.page.InfoPage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TermsPage extends InfoPage {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TermsPage.class);

    public TermsPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public TermsPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public TermsPage(final PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());

    }

    @Override
    protected void addToPage(PageParameters parameters) {
        ContextImage image = new ContextImage("right_side", "/pictures/infopages/shield.jpg"){
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("alt","© Bgartworks | Dreamstime Stock Photos & Stock Free Images");
            }
        };
        add(image);
    }

}
