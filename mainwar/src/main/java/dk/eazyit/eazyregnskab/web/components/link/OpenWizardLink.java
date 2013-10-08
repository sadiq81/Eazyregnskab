package dk.eazyit.eazyregnskab.web.components.link;

import dk.eazyit.eazyregnskab.web.components.modal.wizard.WizardModal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 * @author
 */
public class OpenWizardLink extends AjaxLink {

    WizardModal modal;

    public OpenWizardLink(String id, WizardModal modal) {
        super(id);
        this.modal = modal;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        modal.reset();
        modal.show(target);
    }
}
