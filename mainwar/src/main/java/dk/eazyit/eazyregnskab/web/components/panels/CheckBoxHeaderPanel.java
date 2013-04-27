package dk.eazyit.eazyregnskab.web.components.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author
 */
public class CheckBoxHeaderPanel extends Panel {

    public CheckBoxHeaderPanel(String id) {
        super(id);
        add(new CheckBox("checkbox"));
        add(new Label("label", ""));

    }
}
