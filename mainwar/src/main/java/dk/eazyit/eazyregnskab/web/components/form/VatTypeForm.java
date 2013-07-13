package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2Choice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.validators.forms.VatTypeFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * @author
 */
public class VatTypeForm extends BaseCreateEditForm<VatType> {

    PlaceholderTextField name;

    public VatTypeForm(String id, IModel<VatType> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(name = (PlaceholderTextField) new PlaceholderTextField<String>("name").setRequired(true));
        add(new PlaceholderNumberTextField<Double>("percentage").setMinimum(new Double(0)).setMaximum(new Double(100)).setRequired(true));
        add(new FinanceAccountSelect2Choice("financeAccount").setRequired(true));
        add(new VatTypeFormValidator(name));
    }

    @Override
    protected void configureComponents() {
    }


    @Override
    public void deleteEntity(VatType vatType) {
        if (vatType.getId() != 0) {
            if (financeAccountService.deleteVatType(vatType)) {
                getSession().success(new NotificationMessage(new ResourceModel("vat.type.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                insertNewEntityInModel();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("vat.type.is.in.use")).hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("vat.type.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public VatType buildNewEntity() {
        return new VatType();
    }

    @Override
    public void saveForm(VatType vatType) {
        financeAccountService.saveVatType(vatType, getCurrentLegalEntity());
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel();

    }
}