package dk.eazyit.eazyregnskab.web.components.panels.report;

import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.panels.SessionAwarePanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author
 */
public class ReportPanel extends SessionAwarePanel<ReportObject> {

    protected ReportPanel(String id) {
        super(id);
    }

    protected ReportPanel(String id, IModel<ReportObject> model) {
        super(id, model);
    }

    @Override
    protected void addToPage() {
        add(new Label("nothing.found.with.arguments", new StringResourceModel("nothing.found.with.arguments.${emptyReport}", this, getModel())) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(getModelObject().isEmptyReport());
            }
        }.setOutputMarkupPlaceholderTag(true));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(getModelObject().isSubmitHasBeenPressed());
    }


}
