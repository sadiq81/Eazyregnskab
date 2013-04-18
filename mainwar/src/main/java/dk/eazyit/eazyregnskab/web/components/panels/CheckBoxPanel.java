package dk.eazyit.eazyregnskab.web.components.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class CheckBoxPanel extends Panel {

    public CheckBoxPanel(String componentId, IModel dataModel) {
        super(componentId, dataModel);

        add(new AjaxCheckBox("checkbox",dataModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
    }

}
