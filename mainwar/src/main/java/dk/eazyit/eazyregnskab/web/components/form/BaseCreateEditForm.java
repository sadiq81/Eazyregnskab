package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.BaseEntity;
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

    protected BaseCreateEditForm(String id) {
        super(id);
        addToForm();
    }

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
                saveForm();
                target.add(form.getPage());
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
                target.add(form.getPage());
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Creating new in form " + form.getId() + " with object " + form.getModelObject().toString());
                newEntity();
                target.add(form.getPage());


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
                deleteEntity();
                target.add(form.getPage());
            }
        });
    }

    public abstract void deleteEntity();

    public abstract void newEntity();

    public abstract void saveForm();

}
