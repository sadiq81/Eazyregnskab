package dk.eazyit.eazyregnskab.web.components.panels;

import dk.eazyit.eazyregnskab.util.ReportObject;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public abstract class ReportPanel extends SessionAwarePanel<ReportObject> {

    protected ReportPanel(String id) {
        super(id);
    }

    protected ReportPanel(String id, IModel<ReportObject> model) {
        super(id, model);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(getModelObject().isSubmitHasBeenPressed());
    }
}
