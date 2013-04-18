package dk.eazyit.eazyregnskab.web.components.panels;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public abstract class ActionPanel<T> extends GenericPanel<T> {

    /**
     * @param id    component id
     * @param model model for contact
     */
    public ActionPanel(String id, IModel<T> model) {
        super(id, model);
        add(new AjaxLink("select") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                for (Component c : selectItem()) {
                    target.add(c);
                }
            }
        });

        add(new AjaxLink("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                for (Component c : deleteItem()) {
                    target.add(c);
                }
            }
        });
    }

    protected abstract List<Component> selectItem();

    protected abstract List<Component> deleteItem();
}