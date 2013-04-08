package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.page.InfoPage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ForAdministratorsPage extends InfoPage {
    private static final long serialVersionUID = 1L;

    public ForAdministratorsPage() {
        super();
    }

    public ForAdministratorsPage(IModel<?> model) {
        super(model);
    }

    public ForAdministratorsPage(final PageParameters parameters) {
        super(parameters);

    }

    @Override
    protected void addToPage(PageParameters parameters) {
        ContextImage image = new ContextImage("right_side", "/css/pictures/administrators_right_side.jpg") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("alt", "© Ronfromyork | Stock Free Images & Dreamstime Stock Photos");
            }
        };
        add(image);

    }
}
