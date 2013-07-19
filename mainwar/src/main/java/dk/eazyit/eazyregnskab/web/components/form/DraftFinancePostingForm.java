package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2ChoiceBookableAccounts;
import dk.eazyit.eazyregnskab.web.components.choice.VatTypeDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.validators.forms.DraftFinancePostingFormValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

/**
 * @author
 */
public class DraftFinancePostingForm extends BaseCreateEditForm<DraftFinancePosting> {

    @SpringBean
    FinanceAccountService financeAccountService;

    PlaceholderTextField text;
    private FinanceAccountSelect2ChoiceBookableAccounts financeAccountChoice;
    private FinanceAccountSelect2ChoiceBookableAccounts reverseFinanceAccountChoice;
    private VatTypeDropDownChoice vatTypeChoice;
    private VatTypeDropDownChoice reverseVatTypeChoice;

    public DraftFinancePostingForm(String id, IModel<DraftFinancePosting> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();

        add(new PlaceholderDateField("date", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(reverseFinanceAccountChoice = new FinanceAccountSelect2ChoiceBookableAccounts("reverseFinanceAccount"));
        add(vatTypeChoice = new VatTypeDropDownChoice("vatType"));
        add(new PlaceholderNumberTextField<Double>("amount").setMaximum(new Double(1000000)).setRequired(true));
        add(new PlaceholderNumberTextField<Integer>("bookingNumber").setMaximum(Integer.MAX_VALUE).setRequired(true));
        add(financeAccountChoice = new FinanceAccountSelect2ChoiceBookableAccounts("financeAccount"));
        add(reverseVatTypeChoice = new VatTypeDropDownChoice("reverseVatType"));
        add(text = (PlaceholderTextField) new PlaceholderTextField<String>("text").setRequired(true));

        //TODO VALIDATE VAT combinations and FinanceAccount combinations
        add(new DraftFinancePostingFormValidator(text, financeAccountChoice, reverseFinanceAccountChoice, vatTypeChoice, reverseVatTypeChoice));
    }

    @Override
    protected void configureComponents() {
        configureFinanceAccountChoice();
        configureFinanceAccountChoiceReverse();
    }


    @Override
    public void deleteEntity(DraftFinancePosting draftFinancePosting) {
        financeAccountService.deleteFinancePosting(draftFinancePosting);
        getSession().success(new NotificationMessage(new ResourceModel("finance.posting.was.deleted")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel();
    }

    @Override
    public DraftFinancePosting buildNewEntity() {
        return new DraftFinancePosting(getCurrentDailyLedger().getNextBookingNumber());
    }

    @Override
    public void saveForm(DraftFinancePosting draftFinancePosting) {
        financeAccountService.saveDraftFinancePosting(draftFinancePosting.setDailyLedger(getCurrentDailyLedger()));
        getCurrentDailyLedger().setNextBookingNumber(draftFinancePosting.getBookingNumber() + 1);
        insertNewEntityInModel();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        getModelObject().setBookingNumber(getCurrentDailyLedger().getNextBookingNumber());
    }

    private void configureFinanceAccountChoice() {

        financeAccountChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                FinanceAccount financeAccount = (FinanceAccount) getFormComponent().getModelObject();

                if (financeAccount != null) {
                    FinanceAccount reverse = financeAccount.getStandardReverseFinanceAccount();

                    VatType vatType = financeAccount.getVatType();
                    if (reverse != null) {
                        reverseFinanceAccountChoice.setModelObject(reverse);
                        target.add(reverseFinanceAccountChoice);

                        VatType reverseVatType = reverse.getVatType();
                        if (reverseVatType != null) {
                            reverseVatTypeChoice.setModelObject(reverseVatType);
                            target.add(reverseVatTypeChoice);
                        }
                    }

                    if (vatType != null) {
                        vatTypeChoice.setModelObject(vatType);
                        target.add(vatTypeChoice);
                    }
                }
            }
        });

    }

    private void configureFinanceAccountChoiceReverse() {
        reverseFinanceAccountChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                FinanceAccount financeAccount = (FinanceAccount) getFormComponent().getModelObject();
                if (financeAccount != null) {
                    VatType vatType = financeAccount.getVatType();
                    if (vatType != null) {
                        reverseVatTypeChoice.setModelObject(vatType);
                        target.add(reverseVatTypeChoice);
                    }
                }
            }
        });

    }
}