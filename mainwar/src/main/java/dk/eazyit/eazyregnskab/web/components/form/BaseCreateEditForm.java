package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.*;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.session.SessionAware;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class BaseCreateEditForm<T extends BaseEntity> extends Form<T> implements SessionAware {

    private static final Logger LOG = LoggerFactory.getLogger(BaseCreateEditForm.class);
    protected final static int DURATION = 5;
    protected final static Double DOUBLE_ZERO = new Double(0);

    @SpringBean
    protected LegalEntityService legalEntityService;
    @SpringBean
    protected FinanceAccountService financeAccountService;
    @SpringBean
    protected DailyLedgerService dailyLedgerService;
    @SpringBean
    protected FiscalYearService fiscalYearService;
    @SpringBean
    protected VatTypeService vatTypeService;

    protected BaseCreateEditFormAjaxButton save;
    protected BaseCreateEditFormAjaxButton nev;
    protected BaseCreateEditFormAjaxButton delete;


    protected BaseCreateEditForm(String id, IModel<T> model) {
        super(id, new CompoundPropertyModel<T>(model));
        addToForm();
        configureComponents();
    }


    public void addToForm() {
        add(save = new BaseCreateEditFormAjaxButton("save", new ResourceModel("button.save")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Saving form " + form.getId() + " with object " + form.getModelObject().toString());
                saveForm((T) form.getModelObject());
//                target.add(getPage());
                target.addChildren(getPage(), Form.class);
                target.addChildren(getPage(), DataTable.class);
                target.addChildren(getPage(), FeedbackPanel.class);
                target.focusComponent(focusAfterSave());
            }

        });
        add(nev = new BaseCreateEditFormAjaxButton("new", new ResourceModel("button.new")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Creating new in form " + form.getId() + " with object " + form.getModelObject().toString());
                insertNewEntityInModel((T) form.getModelObject());
                target.add(getPage());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(isNewButtonVisible());
            }
        });
        add(delete = new BaseCreateEditFormAjaxButton("delete", new ResourceModel("button.delete")) {

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

    protected abstract void configureComponents();

    public abstract void deleteEntity(T entity);

    public void insertNewEntityInModel(T previous) {
        setModelObject(buildNewEntity(previous));
    }

    public abstract FormComponent focusAfterSave();

    public abstract T buildNewEntity(T previous);

    public abstract void saveForm(T entity);

    protected boolean isNewButtonVisible() {
        return false;
    }

    protected boolean isDeleteButtonVisible() {
        return false;
    }

    public BaseCreateEditFormAjaxButton getSave() {
        return save;
    }

    public BaseCreateEditFormAjaxButton getNev() {
        return nev;
    }

    public BaseCreateEditFormAjaxButton getDelete() {
        return delete;
    }

    public AppUser getCurrentUser() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentUser();
    }

    public LegalEntity getCurrentLegalEntity() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentLegalEntity();
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        ((EazyregnskabSesssion) Session.get()).setCurrentLegalEntity(legalEntity);
    }

    public DailyLedger getCurrentDailyLedger() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentDailyLedger();
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        ((EazyregnskabSesssion) Session.get()).setCurrentDailyLedger(dailyLedger);
    }


    class BaseCreateEditFormAjaxButton extends AjaxButton {

        BaseCreateEditFormAjaxButton(String id, IModel<String> model) {
            super(id, model);
            add(new LoadingBehavior(new StringResourceModel("button.loading", this, null)));
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

    protected void addToolTipToComponent(Component component, String resourceText) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }

    protected void removeToolTipToComponent(Component component) {
        component.add(AttributeModifier.remove("rel"));
        component.add(AttributeModifier.remove("data-placement"));
        component.add(AttributeModifier.remove("data-original-title"));
    }
}
