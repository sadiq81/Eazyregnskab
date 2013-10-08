package dk.eazyit.eazyregnskab.web.components.modal.wizard;

/**
 * @author
 */

import de.agilecoders.wicket.core.Bootstrap;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class WizardPanel extends Panel {

    private static final String FORM_ID = "form";

    private WizardModal modal;
    private WizardForm form;

    public WizardPanel(String id, WizardModal modal) {
        super(id);
        this.modal = modal;
        add(form = new WizardForm(FORM_ID, modal));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        Bootstrap.renderHead(response);
    }

    public void addPanel(StepPanel panel) {
        form.addPanel(panel);
    }

    public void reset() {
        form.reset();
    }

    public WizardForm getForm() {
        return form;
    }
}
