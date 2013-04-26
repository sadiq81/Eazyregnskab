package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.entities.VatTypeModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * @author
 */
public class VatTypeForm extends BaseCreateEditForm<VatType> {

    public VatTypeForm(String id, IModel<VatType> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(new PlaceholderTextField<String>("name").setRequired(true));
        add(new PlaceholderNumberTextField<Double>("percentage").setMinimum(new Double(0)).setRequired(true));
    }

    @Override
    public void deleteEntity(VatType vatType) {
        super.deleteEntity(vatType);
        if (vatType.getId() != 0) {
            if (financeAccountService.deleteVatType(vatType)) {
                getSession().success(new NotificationMessage(new ResourceModel("vat.type.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                newEntity();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("vat.type.is.in.use")).hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("vat.type.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public CompoundPropertyModel<VatType> newEntity() {
        return new CompoundPropertyModel<VatType>(new VatTypeModel(new VatType()));
    }

    @Override
    public void saveForm(VatType vatType) {
        super.saveForm(null);
        financeAccountService.saveVatType(vatType, getSelectedLegalEntity().getLegalEntityModel().getObject());
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));

    }
}