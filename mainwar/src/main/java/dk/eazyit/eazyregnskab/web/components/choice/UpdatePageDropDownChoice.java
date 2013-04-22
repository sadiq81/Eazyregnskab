package dk.eazyit.eazyregnskab.web.components.choice;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author
 */
public class UpdatePageDropDownChoice<T> extends DropDownChoice<T> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePageDropDownChoice.class);

    public UpdatePageDropDownChoice(String id, List<? extends T> choices) {
        super(id, choices, new ChoiceRenderer<T>("name", "id"));
        init();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    private void init() {
        add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getPage());
            }
        });
    }
}
