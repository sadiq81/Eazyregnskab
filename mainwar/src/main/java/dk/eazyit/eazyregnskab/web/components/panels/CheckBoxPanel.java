package dk.eazyit.eazyregnskab.web.components.panels;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author
 */
@Transactional
public class CheckBoxPanel extends Panel {


    public CheckBoxPanel(String componentId, IModel dataModel) {
        super(componentId, dataModel);
        add(new CheckBox("checkbox", dataModel));
    }




}



