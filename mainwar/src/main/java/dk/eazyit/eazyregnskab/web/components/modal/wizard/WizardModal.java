package dk.eazyit.eazyregnskab.web.components.modal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

/**
 * @author
 */
public abstract class WizardModal extends ModalWindow {

    WizardPanel panel;

    public WizardModal(String id) {
        super(id);
        setContent(panel = getPanel(getContentId()));
        setInitialWidth(800).setMinimalWidth(800);
        setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(getPage());
            }
        });
        setCssClassName(CSS_CLASS_GRAY);
    }

    public void reset() {
        panel.reset();
    }

    public abstract WizardPanel getPanel(String id);

    public WizardPanel getPanel() {
        return panel;
    }
}