package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2Choice;
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
    private FinanceAccountSelect2Choice financeAccountChoice;
    private FinanceAccountSelect2Choice reverseFinanceAccountChoice;
    private VatTypeDropDownChoice vatTypeChoice;

    public DraftFinancePostingForm(String id, IModel<DraftFinancePosting> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();

        add(new PlaceholderDateField("date", new DateTextFieldConfig().autoClose(true).withLanguage("da").withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(reverseFinanceAccountChoice = new FinanceAccountSelect2Choice("reverseFinanceAccount"));
        add(vatTypeChoice = new VatTypeDropDownChoice("vatType"));
        add(financeAccountChoice = new FinanceAccountSelect2Choice("financeAccount"));
        add(new PlaceholderNumberTextField<Double>("amount").setMaximum(new Double(1000000)).setRequired(true));
        add(text = (PlaceholderTextField) new PlaceholderTextField<String>("text").setRequired(true));
        add(new PlaceholderNumberTextField<Integer>("bookingNumber").setMaximum(Integer.MAX_VALUE).setRequired(true));
        add(new DraftFinancePostingFormValidator(text, financeAccountChoice, reverseFinanceAccountChoice, vatTypeChoice));
    }

    @Override
    protected void configureComponents() {
        configureFinanceAccountChoice();
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
                    }
                    if (vatType != null) {
                        vatTypeChoice.setModelObject(vatType);
                        target.add(vatTypeChoice);
                    }
                }
            }
        });

    }
}