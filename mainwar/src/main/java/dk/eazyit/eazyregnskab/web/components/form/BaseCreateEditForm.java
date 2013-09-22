package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.services.*;
import dk.eazyit.eazyregnskab.web.components.button.BaseCreateEditFormAjaxButton;
import dk.eazyit.eazyregnskab.web.components.choice.LegalEntityChooser;
import dk.eazyit.eazyregnskab.web.components.modal.AreYouSureModal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
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
public abstract class BaseCreateEditForm<T extends BaseEntity> extends BaseForm<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseCreateEditForm.class);
    protected final static int DURATION = 5;
    protected final static Double DOUBLE_ZERO = new Double(0);
    private BaseCreateEditForm<T> self;

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

    protected AreYouSureModal saveModalConfirm;
    protected AreYouSureModal newModalConfirm;
    protected AreYouSureModal deleteModalConfirm;

    private FormSettings formSettings;

    protected BaseCreateEditForm(String id, IModel<T> model) {
        super(id, new CompoundPropertyModel<T>(model));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        self = this;
        formSettings = new FormSettings();
        initiateModals();
        addToForm();
        configureComponents();
    }

    private void initiateModals() {
        add(saveModalConfirm = new AreYouSureModal("saveModalConfirm", new ResourceModel(getPage().getClass().getSimpleName() + ".confirm.save").getObject()) {
            @Override
            protected void onConfirm(AjaxRequestTarget target) {
                saveForm(self.getModelObject());
                updatePageComponents(target);
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                updatePageComponents(target);
            }
        });
        add(newModalConfirm = new AreYouSureModal("newModalConfirm", new ResourceModel(getPage().getClass().getSimpleName() + ".confirm.new").getObject()) {
            @Override
            protected void onConfirm(AjaxRequestTarget target) {
                insertNewEntityInModel(self.getModelObject());
                updatePageComponents(target);
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                updatePageComponents(target);
            }
        });
        add(deleteModalConfirm = new AreYouSureModal("deleteModalConfirm", new ResourceModel(getPage().getClass().getSimpleName() + ".confirm.delete").getObject()) {
            @Override
            protected void onConfirm(AjaxRequestTarget target) {
                deleteEntity(self.getModelObject());
                updatePageComponents(target);
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                updatePageComponents(target);
            }
        });

    }


    public void addToForm() {
        add(new BaseCreateEditFormAjaxButton("save", new ResourceModel("button.save")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Saving form " + form.getId() + " with object " + form.getModelObject().toString());
                if (formSettings.isConfirmSave()) {
                    saveModalConfirm.show(target);
                } else {
                    saveForm(self.getModelObject());
                    updatePageComponents(target);
                }
            }

        });
        add(new BaseCreateEditFormAjaxButton("new", new ResourceModel("button.new")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Creating new in form " + form.getId() + " with object " + form.getModelObject().toString());
                if (formSettings.isConfirmNew()) {
                    newModalConfirm.show(target);
                } else {
                    insertNewEntityInModel(self.getModelObject());
                    updatePageComponents(target);
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(formSettings.isNewVisible());
            }
        });
        add(new BaseCreateEditFormAjaxButton("delete", new ResourceModel("button.delete")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                LOG.debug("Deleting object in form " + form.getId() + " with object " + form.getModelObject().toString());
                if (formSettings.isConfirmNew()) {
                    deleteModalConfirm.show(target);
                } else {
                    deleteEntity(self.getModelObject());
                    updatePageComponents(target);
                }
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(formSettings.isDeleteVisible());
            }
        });
    }

    private void updatePageComponents(AjaxRequestTarget target) {
        target.addChildren(getPage(), Form.class);
        target.addChildren(getPage(), DataTable.class);
        target.addChildren(getPage(), FeedbackPanel.class);
        target.addChildren(getPage(), LegalEntityChooser.class);
        target.focusComponent(focusAfterSave());
    }

    protected abstract void configureComponents();

    public void insertNewEntityInModel(T previous) {
        setModelObject(buildNewEntity(previous));
    }

    public abstract void deleteEntity(T entity);

    public abstract FormComponent focusAfterSave();

    public abstract T buildNewEntity(T previous);

    public abstract void saveForm(T entity);

    public FormSettings getFormSettings() {
        return formSettings;
    }
}
