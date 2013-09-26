package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.domain.EntityWithLongId;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.PostingService;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2ChoiceBookableAccounts;
import dk.eazyit.eazyregnskab.web.components.choice.VatTypeDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.validators.forms.DraftFinancePostingFormValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class DraftFinancePostingForm extends BaseCreateEditForm<DraftFinancePosting> {

    @SpringBean
    PostingService postingService;

    private static final Logger LOG = LoggerFactory.getLogger(DraftFinancePostingForm.class);


    PlaceholderDateField date;
    PlaceholderTextField text;
    private FinanceAccountSelect2ChoiceBookableAccounts financeAccountChoice;
    private FinanceAccountSelect2ChoiceBookableAccounts reverseFinanceAccountChoice;
    private VatTypeDropDownChoice vatTypeChoice;
    private VatTypeDropDownChoice reverseVatTypeChoice;

    public DraftFinancePostingForm(String id, IModel<DraftFinancePosting> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    public void addToForm() {
        super.addToForm();

        add(date = (PlaceholderDateField) new PlaceholderDateField("date", "BookkeepingPage", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true).setOutputMarkupId(true));
        add(reverseFinanceAccountChoice = new FinanceAccountSelect2ChoiceBookableAccounts("reverseFinanceAccount"));
        add(vatTypeChoice = (VatTypeDropDownChoice) new VatTypeDropDownChoice("vatType").setNullValid(true));
        add(new PlaceholderNumberTextField<Double>("amount", "BookkeepingPage").setMaximum(new Double(1000000)).setRequired(true));
        add(new PlaceholderNumberTextField<Integer>("bookingNumber", "BookkeepingPage").setMaximum(Integer.MAX_VALUE).setRequired(true));
        add(financeAccountChoice = new FinanceAccountSelect2ChoiceBookableAccounts("financeAccount"));
        add(reverseVatTypeChoice = (VatTypeDropDownChoice) new VatTypeDropDownChoice("reverseVatType").setNullValid(true));
        add(text = (PlaceholderTextField) new PlaceholderTextField<String>("text", "BookkeepingPage").setRequired(true));
        add(new DraftFinancePostingFormValidator(text, financeAccountChoice, reverseFinanceAccountChoice, vatTypeChoice, reverseVatTypeChoice));
    }

    @Override
    protected void configureComponents() {
        configureFinanceAccountChoice();
        configureFinanceAccountChoiceReverse();
    }


    @Override
    public void deleteEntity(DraftFinancePosting draftFinancePosting) {
        postingService.deleteFinancePosting(draftFinancePosting);
        getSession().success(new NotificationMessage(new ResourceModel("BookkeepingPage.finance.posting.was.deleted")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel(draftFinancePosting);
    }

    @Override
    public DraftFinancePosting buildNewEntity(DraftFinancePosting previous) {

        if (DOUBLE_ZERO.equals(dailyLedgerService.checkBalanceOfDailyLedger(getCurrentDailyLedger()))) {
            getCurrentDailyLedger().setNextBookingNumber(previous.getBookingNumber() + 1);
            dailyLedgerService.saveDailyLedger(getCurrentDailyLedger(), getCurrentLegalEntity());
            return new DraftFinancePosting(getCurrentDailyLedger().getNextBookingNumber()).setDate(previous.getDate());
        } else {
            return new DraftFinancePosting().setDate(previous.getDate()).setBookingNumber(previous.getBookingNumber()).setText(previous.getText());
        }
    }

    @Override
    public void saveForm(DraftFinancePosting draftFinancePosting) {
        LOG.debug("Saving draft financeposting form " + draftFinancePosting);
        postingService.saveDraftFinancePosting(draftFinancePosting.setDailyLedger(getCurrentDailyLedger()));
        insertNewEntityInModel(draftFinancePosting);
    }

    @Override
    public FormComponent focusAfterSave() {
        return date;
    }

    private void configureFinanceAccountChoice() {

        financeAccountChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                FinanceAccount financeAccount = (FinanceAccount) getFormComponent().getModelObject();
                if (financeAccount != null) {

                    FinanceAccount ledgerReverse = getCurrentDailyLedger().getFinanceAccount();

                    if (ledgerReverse != null) {

                        reverseFinanceAccountChoice.setModelObject(ledgerReverse);

                    } else {

                        FinanceAccount reverse = financeAccount.getStandardReverseFinanceAccount();
                        if (reverse != null) {
                            reverseFinanceAccountChoice.setModelObject(reverse);

                            VatType reverseVatType = reverse.getVatType();
                            if (reverseVatType != null) {
                                reverseVatTypeChoice.setModelObject(reverseVatType);
                            }
                        }
                    }

                    VatType vatType = financeAccount.getVatType();
                    if (vatType != null) {
                        vatTypeChoice.setModelObject(vatType);
                    } else {
                        vatTypeChoice.setModelObject(null);

                    }

                }
                target.add(vatTypeChoice, reverseFinanceAccountChoice, reverseVatTypeChoice);
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
                    } else {
                        reverseVatTypeChoice.setModelObject(null);
                    }
                    target.add(reverseVatTypeChoice);

                }
            }
        });

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