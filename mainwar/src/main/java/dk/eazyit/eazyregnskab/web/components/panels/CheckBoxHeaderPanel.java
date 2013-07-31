package dk.eazyit.eazyregnskab.web.components.panels;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * @author
 */
public class CheckBoxHeaderPanel extends Panel {

    public CheckBoxHeaderPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CheckBox("checkbox", new PropertyModel<Boolean>(getPage(), "selected")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                getPage().visitChildren(new IVisitor<Component, Object>() {
                    @Override
                    public void component(Component object, IVisit<Object> visit) {
                        if (object instanceof CheckBoxPanel){
                            int i = 0;
                        }
                    }
                });

                target.add(getPage());
            }
        }));
        add(new Label("label", ""));
    }
}
