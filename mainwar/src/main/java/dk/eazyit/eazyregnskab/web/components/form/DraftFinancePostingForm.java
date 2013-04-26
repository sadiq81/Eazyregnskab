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
import dk.eazyit.eazyregnskab.web.components.models.DraftFinancePostingModel;
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

    private FinanceAccountSelect2Choice reverseFinanceAccountChoice;
    private VatTypeDropDownChoice vatTypeChoice;

    public DraftFinancePostingForm(String id, IModel<DraftFinancePosting> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();

        add(new PlaceholderDateField("date", new DateTextFieldConfig().autoClose(true).withLanguage("da").withFormat("dd-MM-yyyy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(reverseFinanceAccountChoice = new FinanceAccountSelect2Choice("reverseFinanceAccount"));
        add(vatTypeChoice = new VatTypeDropDownChoice("vatType"));
        add(new FinanceAccountSelect2Choice("financeAccount", reverseFinanceAccountChoice, vatTypeChoice));
        add(new PlaceholderNumberTextField<Double>("amount").setMinimum(new Double(0)).setRequired(true));
        add(new PlaceholderTextField<String>("text"));
        add(new PlaceholderNumberTextField<Integer>("bookingNumber").setRequired(true));
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