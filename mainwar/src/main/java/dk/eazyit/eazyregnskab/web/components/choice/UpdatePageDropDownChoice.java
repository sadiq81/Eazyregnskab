package dk.eazyit.eazyregnskab.web.components.choice;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;

import java.util.List;

/**
 * @author
 */
public class UpdatePageDropDownChoice<T> extends DropDownChoice<T> {

    public UpdatePageDropDownChoice(String id, List<? extends T> choices) {
        super(id, choices, new ChoiceRenderer<T>("name", "id"));
        init();
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
