package dk.eazyit.eazyregnskab.web.components.modal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public abstract class StepPanel extends Panel {

    private static final String VIEW_ID = "view";

    public StepPanel(IModel<String> title, IModel<String> description) {
        super(VIEW_ID);
        setOutputMarkupPlaceholderTag(true);
        add(new Label("title", title));
        add(new MultiLineLabel("description", description));
    }

    public boolean isNextAvailable() {
        return true;
    }

    public boolean isPreviousAvailable() {
        return true;
    }

    public boolean isFinishAvailable() {
        return true;
    }

    public void onNext(AjaxRequestTarget target) {

    }
}