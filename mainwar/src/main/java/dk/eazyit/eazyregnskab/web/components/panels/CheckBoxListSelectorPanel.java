package dk.eazyit.eazyregnskab.web.components.panels;

import dk.eazyit.eazyregnskab.web.components.input.CheckBoxListSelector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

/**
 * @author
 */
public class CheckBoxListSelectorPanel extends Panel {

    public CheckBoxListSelectorPanel(String id, List<CheckBox> checkBoxList) {
        super(id);
        add(new CheckBoxListSelector("checkbox", checkBoxList));
        add(new Label("label", ""));

    }
}
