package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.session.CurrentDailyLedger;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import dk.eazyit.eazyregnskab.session.CurrentUser;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class BaseCreateEditForm<T extends BaseEntity> extends Form<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseCreateEditForm.class);
    protected final static int DURATION = 5;

    protected BaseCreateEditForm(String id, IModel<T> model) {
        super(id, new CompoundPropertyModel<T>(model));
        addToForm();
    }

    public void addToForm() {
        add(new AjaxButton("save", new ResourceModel("button.save")) {

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("error on save in form " + form.getId() + " with object " + form.getModelObject().toString());
                target.add(form);
                FeedbackPanel fp = (FeedbackPanel) form.getPage().get("feedback");
                if (fp != null) {
                    target.add(fp);
                }
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Saving form " + form.getId() + " with object " + form.getModelObject().toString());
                saveForm((T) form.getModelObject());
                target.add(getPage());
            }
        });
        add(new AjaxButton("new", new ResourceModel("button.new")) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("error on new in form " + form.getId() + " with object " + form.getModelObject().toString());
                FeedbackPanel fp = (FeedbackPanel) form.getPage().get("feedback");
                if (fp != null) {
                    target.add(fp);
                }
                target.add(getPage());
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Creating new in form " + form.getId() + " with object " + form.getModelObject().toString());
                form.setDefaultModel(newEntity());
                target.add(getPage());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(isNewButtonVisible());
            }
        });
        add(new AjaxButton("delete", new ResourceModel("button.delete")) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("error on delete in form " + form.getId() + " with object " + form.getModelObject().toString());
                target.add(form);
                FeedbackPanel fp = (FeedbackPanel) form.getPage().get("feedback");
                if (fp != null) {
                    target.add(fp);
                }
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Deleting object in form " + form.getId() + " with object " + form.getModelObject().toString());
                deleteEntity((T) form.getModelObject());
                target.add(getPage());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(isDeleteButtonVisible());
            }
        });
    }

    public void deleteEntity(T entity) {
        if (entity.equals(getModelObject())) {
            setDefaultModel(newEntity());
        }
    }

    public abstract CompoundPropertyModel<T> newEntity();

    public void saveForm(T entity) {
        setDefaultModel(newEntity());
    }

    protected boolean isNewButtonVisible() {
        return false;
    }

    protected boolean isDeleteButtonVisible() {
        return false;
    }

    protected CurrentUser getCurrentUser() {
        return (CurrentUser) getSession().getAttribute(CurrentUser.ATTRIBUTE_NAME);
    }

    protected CurrentLegalEntity getSelectedLegalEntity() {
        return (CurrentLegalEntity) getSession().getAttribute(CurrentLegalEntity.ATTRIBUTE_NAME);
    }

    protected CurrentDailyLedger getCurrentDailyLedger() {
        return (CurrentDailyLedger) getSession().getAttribute(CurrentDailyLedger.ATTRIBUTE_NAME);
    }

}
