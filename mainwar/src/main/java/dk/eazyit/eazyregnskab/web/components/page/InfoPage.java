package dk.eazyit.eazyregnskab.web.components.page;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Eazy IT
 */
public abstract class InfoPage extends AppBasePage {

    public InfoPage() {
        super();
    }

    public InfoPage(IModel<?> model) {
        super(model);
    }

    public InfoPage(PageParameters parameters) {
        super(parameters);
    }

    protected void addToPage() {
        ContextImage image = new ContextImage("right_side", "/css/pictures/default_right_side.jpg") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("alt", "© Dotsdesign | Stock Free Images & Dreamstime Stock Photos");
            }
        };
        add(image);


    }
}
