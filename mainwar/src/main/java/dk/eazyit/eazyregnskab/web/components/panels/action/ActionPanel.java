package dk.eazyit.eazyregnskab.web.components.panels.action;

import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.link.LoadingAjaxLink;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author
 */
public abstract class ActionPanel<T extends BaseEntity> extends GenericPanel<T> {

    protected final static int DURATION = 5;

    LoadingAjaxLink select;
    LoadingAjaxLink delete;

    /**
     * @param id    component id
     * @param model model for contact
     */
    public ActionPanel(String id, IModel<T> model) {
        super(id, model);
        add(select = new LoadingAjaxLink("select") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                BaseCreateEditForm form = selectItem(getItem());
                target.focusComponent(form);
                target.appendJavaScript("$('html, body').animate({ scrollTop: 0 }, 'slow');");
                target.add(form, select);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(selectAllowed());
            }
        });
        addToolTipToComponent(select, "button.edit");

        add(delete = new LoadingAjaxLink("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                deleteItem((T) getParent().getDefaultModelObject());
                target.add(getPage(), delete);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(deleteAllowed());
            }
        });
        addToolTipToComponent(delete, "button.delete");
    }

    protected abstract BaseCreateEditForm<T> selectItem(T entity);

    protected abstract void deleteItem(T entity);

    protected T getItem() {
        return (T) getDefaultModelObject();
    }

    protected boolean selectAllowed() {
        return true;
    }

    protected boolean deleteAllowed() {
        return true;
    }

    protected void addToolTipToComponent(Component component, String resourceText) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }
}