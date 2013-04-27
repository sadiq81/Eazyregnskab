package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2Choice;
import dk.eazyit.eazyregnskab.web.components.choice.VatTypeDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.entities.DraftFinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.validators.forms.DraftFinancePostingFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
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
        add(financeAccountChoice = new FinanceAccountSelect2Choice("financeAccount", reverseFinanceAccountChoice, vatTypeChoice));
        add(new PlaceholderNumberTextField<Double>("amount").setMaximum(new Double(1000000)).setRequired(true));
        add(text = (PlaceholderTextField) new PlaceholderTextField<String>("text").setRequired(true));
        add(new PlaceholderNumberTextField<Integer>("bookingNumber").setMaximum(Integer.MAX_VALUE).setRequired(true));
        add(new DraftFinancePostingFormValidator(text, financeAccountChoice, reverseFinanceAccountChoice, vatTypeChoice));
    }

    @Override
    public void deleteEntity(DraftFinancePosting draftFinancePosting) {
        super.deleteEntity(draftFinancePosting);
        financeAccountService.deleteFinancePosting(draftFinancePosting);
        getSession().success(new NotificationMessage(new ResourceModel("finance.posting.was.deleted")).hideAfter(Duration.seconds(DURATION)));
    }

    @Override
    public CompoundPropertyModel<DraftFinancePosting> newEntity() {
        return new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting()));
    }

    @Override
    public void saveForm(DraftFinancePosting draftFinancePosting) {
        super.saveForm(null);
        financeAccountService.saveDraftFinancePosting(draftFinancePosting.setDailyLedger(getCurrentDailyLedger().getDailyLedgerModel().getObject()));
    }
}