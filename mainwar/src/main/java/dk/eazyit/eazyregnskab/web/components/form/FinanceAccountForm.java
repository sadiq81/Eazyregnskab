package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import java.util.Arrays;

/**
 * @author
 */
public class FinanceAccountForm extends BaseCreateEditForm<FinanceAccount> {

    public FinanceAccountForm(String id, IModel<FinanceAccount> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(new PlaceholderTextField<String>("name").setRequired(true));
        add(new PlaceholderTextField<Integer>("accountNumber").setRequired(true));
        add(new DropDownChoice<VatType>("vatType", financeAccountService.findAllVatTypesForLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()), new ChoiceRenderer<VatType>("name", "id")));
        add(new DropDownChoice<FinanceAccount>("standardReverseFinanceAccount", financeAccountService.findFinanceAccountByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()), new ChoiceRenderer<FinanceAccount>("name", "id")));
        add(new EnumDropDownChoice<FinanceAccountType>("financeAccountType", Arrays.asList(FinanceAccountType.values())).setRequired(true));
    }

    @Override
    public void deleteEntity(FinanceAccount financeAccount) {
        super.deleteEntity(financeAccount);
        if (financeAccount.getId() != 0) {
            if (financeAccountService.deleteFinanceAccount(financeAccount)) {
                getSession().success(new NotificationMessage(new ResourceModel("finance.account.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                newEntity();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("finance.account.is.in.use")).hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("finance.account.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public CompoundPropertyModel<FinanceAccount> newEntity() {
        return new CompoundPropertyModel<FinanceAccount>(new FinanceAccount());
    }

    @Override
    public void saveForm(FinanceAccount financeAccount) {
        super.saveForm(null);
        financeAccountService.saveFinanceAccount(financeAccount, getSelectedLegalEntity().getLegalEntityModel().getObject());
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));

    }
}