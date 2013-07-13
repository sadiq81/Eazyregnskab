package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class BaseCreateEditForm<T extends BaseEntity> extends Form<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseCreateEditForm.class);
    protected final static int DURATION = 5;

    @SpringBean
    protected LegalEntityService legalEntityService;
    @SpringBean
    protected FinanceAccountService financeAccountService;

    protected BaseCreateEditForm(String id, IModel<T> model) {
        super(id, new CompoundPropertyModel<T>(model));
        addToForm();
    }

    public void addToForm() {
        add(new AjaxButton("save", new ResourceModel("button.save")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Saving form " + form.getId() + " with object " + form.getModelObject().toString());
                saveForm((T) form.getModelObject());
                target.add(getPage());
            }

        });
        add(new AjaxButton("new", new ResourceModel("button.new")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Creating new in form " + form.getId() + " with object " + form.getModelObject().toString());
                insertNewEntityInModel();
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

    public abstract void deleteEntity(T entity);

    public void insertNewEntityInModel(){
        setModel(new CompoundPropertyModel<T>(buildNewEntity()));
    }

    public abstract T buildNewEntity();

    public abstract void saveForm(T entity);

    protected boolean isNewButtonVisible() {
        return false;
    }

    protected boolean isDeleteButtonVisible() {
        return false;
    }

    protected AppUser getCurrentUser() {
        return (AppUser) getSession().getAttribute(AppUser.ATTRIBUTE_NAME);
    }

    protected LegalEntity getCurrentLegalEntity() {
        return (LegalEntity) getSession().getAttribute(LegalEntity.ATTRIBUTE_NAME);
    }

    protected void setCurrentLegalEntity(LegalEntity legalEntity) {
        getSession().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);
    }

    protected DailyLedger getCurrentDailyLedger() {
        return (DailyLedger) getSession().getAttribute(DailyLedger.ATTRIBUTE_NAME);
    }

    protected void setCurrentDailyLedger(DailyLedger dailyLedger) {
        getSession().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);
    }

    class BaseCreateEditFormAjaxButton extends AjaxButton {

        BaseCreateEditFormAjaxButton(String id) {
            super(id);
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            LOG.debug("error on delete in form " + form.getId() + " with object " + form.getModelObject().toString());
            target.add(form);
            FeedbackPanel fp = (FeedbackPanel) form.getPage().get("feedback");
            if (fp != null) {
                target.add(fp);
            }
        }
    }

}
