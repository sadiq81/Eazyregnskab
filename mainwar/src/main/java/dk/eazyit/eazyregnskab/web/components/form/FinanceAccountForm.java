package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author
 */
public class FinanceAccountForm extends BaseCreateEditForm<FinanceAccount> {

    private static final ArrayList<FinanceAccountType> lockedTypes = new ArrayList<FinanceAccountType>(Arrays.asList(FinanceAccountType.HEADLINE, FinanceAccountType.SUM));

    DropDownChoice<VatType> vatType;
    EnumDropDownChoice<FinanceAccountType> financeAccountType;
    DropDownChoice<FinanceAccount> standardReverseFinanceAccount;
    DropDownChoice<FinanceAccount> sumFrom;
    DropDownChoice<FinanceAccount> sumTo;

    public FinanceAccountForm(String id, IModel<FinanceAccount> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(new PlaceholderTextField<String>("name").setRequired(true));
        add(new PlaceholderTextField<Integer>("accountNumber").setRequired(true));
        add(vatType = new DropDownChoice<VatType>("vatType", financeAccountService.findAllVatTypesForLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<VatType>("name", "id")));
        vatType.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        }).setOutputMarkupPlaceholderTag(true);

        add(financeAccountType = new EnumDropDownChoice<FinanceAccountType>("financeAccountType", Arrays.asList(FinanceAccountType.values())));
        financeAccountType.setRequired(true);
        financeAccountType.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                FinanceAccount f = getModelObject();
                target.add(vatType);
                target.add(standardReverseFinanceAccount);
                target.add(sumFrom);
                target.add(sumTo);

                if (lockedTypes.contains((((EnumDropDownChoice<FinanceAccountType>) this.getFormComponent()).getConvertedInput()))) {
                    vatType.setEnabled(false).setDefaultModelObject(null);
                    standardReverseFinanceAccount.setEnabled(false).setDefaultModelObject(null);
                } else {
                    vatType.setEnabled(true);
                    standardReverseFinanceAccount.setEnabled(true);
                    sumFrom.setDefaultModelObject(null);
                    sumTo.setDefaultModelObject(null);
                }
            }
        });

        add(standardReverseFinanceAccount = new DropDownChoice<FinanceAccount>("standardReverseFinanceAccount", financeAccountService.findBookableFinanceAccountByLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<FinanceAccount>("name", "id")));
        standardReverseFinanceAccount.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        standardReverseFinanceAccount.setOutputMarkupPlaceholderTag(true);

        add(sumFrom = new DropDownChoice<FinanceAccount>("sumFrom", financeAccountService.findBookableFinanceAccountByLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<FinanceAccount>("name", "id")) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(financeAccountType.getConvertedInput() == FinanceAccountType.SUM);
            }
        });
        sumFrom.setOutputMarkupPlaceholderTag(true);
        add(sumTo = new DropDownChoice<FinanceAccount>("sumTo", financeAccountService.findBookableFinanceAccountByLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<FinanceAccount>("name", "id")) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(financeAccountType.getConvertedInput() == FinanceAccountType.SUM);
            }
        });
        sumTo.setOutputMarkupPlaceholderTag(true);
    }

    @Override
    public void deleteEntity(FinanceAccount financeAccount) {
        if (financeAccount.getId() != 0) {
            if (financeAccountService.deleteFinanceAccount(financeAccount)) {
                getSession().success(new NotificationMessage(new ResourceModel("finance.account.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                insertNewEntityInModel();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("finance.account.is.in.use")).hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("finance.account.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public FinanceAccount buildNewEntity() {
        return new FinanceAccount();
    }

    @Override
    public void saveForm(FinanceAccount financeAccount) {
        financeAccountService.saveFinanceAccount(financeAccount, getCurrentLegalEntity());
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel();

    }
}