package dk.eazyit.eazyregnskab.web.components.panels;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class CheckBoxPanel extends Panel {

    CheckBox checkbox;

    public CheckBoxPanel(String componentId, IModel dataModel) {
        super(componentId, dataModel);

        add(checkbox = new CheckBox("checkbox",dataModel));
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }
}
