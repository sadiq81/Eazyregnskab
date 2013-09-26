package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.EntityWithLongId;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2ChoiceBookableAccounts;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.validators.forms.VatTypeFormValidator;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class VatTypeForm extends BaseCreateEditForm<VatType> {

    PlaceholderTextField name;

    private static final Logger LOG = LoggerFactory.getLogger(VatTypeForm.class);

    public VatTypeForm(String id, IModel<VatType> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(name = (PlaceholderTextField) new PlaceholderTextField<String>("name", "VatTypesPage").setRequired(true));
        add(new PlaceholderNumberTextField<Double>("percentage", "VatTypesPage").setMinimum(new Double(0)).setMaximum(new Double(100)).setRequired(true));
        add(new FinanceAccountSelect2ChoiceBookableAccounts("financeAccount").setRequired(true));
        add(new FinanceAccountSelect2ChoiceBookableAccounts("financeAccountReverse"));
        add(new VatTypeFormValidator(name));
    }

    @Override
    protected void configureComponents() {
    }


    @Override
    public void deleteEntity(VatType vatType) {
        if (vatType.getId() != 0) {
            if (vatTypeService.isDeleteVatTypeAllowed(vatType)) {
                getSession().success(new NotificationMessage(new ResourceModel("VatTypesPage.vat.type.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                insertNewEntityInModel(vatType);
                vatTypeService.deleteVatType(vatType);
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("VatTypesPage.vat.type.is.in.use")).hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("VatTypesPage.vat.type.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public VatType buildNewEntity(VatType previous) {
        return new VatType();
    }

    @Override
    public void saveForm(VatType vatType) {
        LOG.debug("saving vat form " + vatType);
        vatTypeService.saveVatType(vatType, getCurrentLegalEntity());
        getSession().success(new NotificationMessage(new ResourceModel("VatTypesPage.changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel(vatType);
    }


    @Override
    public FormComponent focusAfterSave() {
        return name;
    }

    @Override
    protected void addReports() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected boolean exportWithBeans() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected EntityWithLongId[] getCollectionForReport() {
        return new EntityWithLongId[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}