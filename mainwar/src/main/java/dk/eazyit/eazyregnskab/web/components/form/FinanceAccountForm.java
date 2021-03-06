package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountTypeDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.label.financeaccountform.SumLabel;
import dk.eazyit.eazyregnskab.web.components.validators.forms.FinanceAccountFormValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author
 */
public class FinanceAccountForm extends BaseCreateEditForm<FinanceAccount> {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountForm.class);
    private static final ArrayList<FinanceAccountType> lockedTypes = new ArrayList<FinanceAccountType>(Arrays.asList(FinanceAccountType.HEADLINE, FinanceAccountType.SUM, FinanceAccountType.CATEGORY));

    PlaceholderTextField name;
    FormComponent<Integer> accountNumber;
    DropDownChoice<VatType> vatType;
    FinanceAccountTypeDropDownChoice financeAccountType;
    DropDownChoice<FinanceAccount> standardReverseFinanceAccount;
    DropDownChoice<FinanceAccount> sumFrom;
    SumLabel sumFromLabel;
    DropDownChoice<FinanceAccount> sumTo;
    SumLabel sumToLabel;
    CheckBox locked;
    Label lockedInfo;

    public FinanceAccountForm(String id, IModel<FinanceAccount> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(name = (PlaceholderTextField) new PlaceholderTextField<String>("name", "ChartOfAccountsPage").setRequired(true));
        add(accountNumber = new PlaceholderTextField<Integer>("accountNumber", "ChartOfAccountsPage").setRequired(true));
        add(vatType = (DropDownChoice<VatType>) new DropDownChoice<>("vatType", vatTypeService.findAllVatTypesForLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<VatType>("name", "id")).setOutputMarkupPlaceholderTag(true));
        add(financeAccountType = (FinanceAccountTypeDropDownChoice) new FinanceAccountTypeDropDownChoice("financeAccountType", FinanceAccountType.getNonSystemAccounts()).setRequired(true));
        add(standardReverseFinanceAccount = (DropDownChoice<FinanceAccount>) new DropDownChoice<FinanceAccount>("standardReverseFinanceAccount", financeAccountService.findBookableFinanceAccountByLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<FinanceAccount>("name", "id")).setOutputMarkupPlaceholderTag(true));
        add(sumFrom = (DropDownChoice<FinanceAccount>) new FinanceAccountDropDownChoice<FinanceAccount>("sumFrom", financeAccountService.findFinanceAccountByLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<FinanceAccount>("name", "id")).setOutputMarkupPlaceholderTag(true));
        add(sumFromLabel = (SumLabel) new SumLabel("sumFromLabel", new ResourceModel("ChartOfAccountsPage.sum.from")).setOutputMarkupPlaceholderTag(true));
        add(sumTo = (DropDownChoice<FinanceAccount>) new FinanceAccountDropDownChoice<FinanceAccount>("sumTo", financeAccountService.findFinanceAccountByLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<FinanceAccount>("name", "id")).setOutputMarkupPlaceholderTag(true));
        add(sumToLabel = (SumLabel) new SumLabel("sumToLabel", new ResourceModel("ChartOfAccountsPage.sum.to")).setOutputMarkupPlaceholderTag(true));
        add(locked = new CheckBox("locked"));
        add(lockedInfo = new Label("lockedInfo", new ResourceModel("cannot.be.changed.in.use")));
        add(new FinanceAccountFormValidator(accountNumber));
    }

    @Override
    public void deleteEntity(FinanceAccount financeAccount) {
        if (financeAccount.getId() != 0) {
            if (financeAccountService.isDeletingFinanceAccountAllowed(financeAccount)) {
                getSession().success(new NotificationMessage(new ResourceModel("ChartOfAccountsPage.finance.account.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                insertNewEntityInModel(financeAccount);
                financeAccountService.deleteFinanceAccount(financeAccount);
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("ChartOfAccountsPage.finance.account.is.in.use")).hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("ChartOfAccountsPage.finance.account.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public FinanceAccount buildNewEntity(FinanceAccount previous) {
        return new FinanceAccount();
    }

    @Override
    public void saveForm(FinanceAccount financeAccount) {
        LOG.debug("saving finance account form " + financeAccount);
        financeAccountService.saveFinanceAccount(financeAccount, getCurrentLegalEntity());
        getSession().success(new NotificationMessage(new ResourceModel("ChartOfAccountsPage.changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel(financeAccount);
    }

    @Override
    public FormComponent focusAfterSave() {
        return accountNumber;
    }

    @Override
    protected void configureComponents() {
        configureFinanceAccountType();
    }


    private void configureFinanceAccountType() {

        financeAccountType.setNullValid(false);
        financeAccountType.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                getFormComponent().processInput();

                if (lockedTypes.contains((((EnumDropDownChoice<FinanceAccountType>) this.getFormComponent()).getConvertedInput()))) {
                    vatType.setEnabled(false).setDefaultModelObject(null);
                    standardReverseFinanceAccount.setEnabled(false).setDefaultModelObject(null);
                    sumFrom.setDefaultModelObject(null);
                    sumTo.setDefaultModelObject(null);
                    target.add(vatType, standardReverseFinanceAccount, sumFrom, sumTo, sumFromLabel, sumToLabel);
                } else {
                    if (!vatType.isEnabled()) {
                        vatType.setEnabled(true);
                        target.add(vatType);
                    }
                    if (!standardReverseFinanceAccount.isEnabled()) {
                        standardReverseFinanceAccount.setEnabled(true);
                        target.add(standardReverseFinanceAccount);
                    }
                }
                target.add(sumFrom, sumTo, sumFromLabel, sumToLabel);

            }
        });
    }

    @Override
    protected void addReports() {
        add(getJasperPdfResourceLink("exportPdf", getJasperReportName(), getString(getJasperReportName() + ".pdf")));
        add(getJasperXlsResourceLink("exportXls", getJasperReportName(), getString(getJasperReportName() + ".xls")));
    }

    @Override
    protected void onBeforeRender() {

        lockedInfo.setVisibilityAllowed(getModelObject().isInUse());
        accountNumber.setEnabled(!getModelObject().isInUse());
        vatType.setEnabled(getModelObject().isVatAccount());
        financeAccountType.setEnabled(!getModelObject().isSystemAccount() && !getModelObject().isInUse());
        standardReverseFinanceAccount.setEnabled(getModelObject().canHaveStandardReverseAccount());
        locked.setEnabled(!getModelObject().isSystemAccount());
        sumFrom.setRequired(FinanceAccountType.SUM.equals(getModelObject().getFinanceAccountType()));
        sumTo.setRequired(FinanceAccountType.SUM.equals(getModelObject().getFinanceAccountType()));
        super.onBeforeRender();
    }

}