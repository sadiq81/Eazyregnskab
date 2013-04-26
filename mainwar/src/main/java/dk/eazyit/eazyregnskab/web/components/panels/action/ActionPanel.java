package dk.eazyit.eazyregnskab.web.components.panels.action;

import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public abstract class ActionPanel<T extends BaseEntity> extends GenericPanel<T> {

    /**
     * @param id    component id
     * @param model model for contact
     */
    public ActionPanel(String id, IModel<T> model) {
        super(id, model);
        add(new AjaxLink("select") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(selectItem((T) getParent().getDefaultModelObject()));
            }
        });

        add(new AjaxLink("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                deleteItem((T) getParent().getDefaultModelObject());
                target.add(getPage());
            }
        });
    }

    protected abstract BaseCreateEditForm<T> selectItem(T entity);

    protected abstract void deleteItem(T entity);
}