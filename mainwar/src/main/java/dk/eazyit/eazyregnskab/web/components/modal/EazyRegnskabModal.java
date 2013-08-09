package dk.eazyit.eazyregnskab.web.components.modal;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class EazyRegnskabModal extends ModalWindow {



    public EazyRegnskabModal(String id) {
        super(id);
    }

    public EazyRegnskabModal(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public ModalWindow setPageCreator(PageCreator creator) {
        return super.setPageCreator(creator);
    }

    @Override
    public ModalWindow setCloseButtonCallback(CloseButtonCallback callback) {
        return super.setCloseButtonCallback(callback);
    }

    @Override
    public ModalWindow setWindowClosedCallback(WindowClosedCallback callback) {
        return super.setWindowClosedCallback(callback);
    }
}
