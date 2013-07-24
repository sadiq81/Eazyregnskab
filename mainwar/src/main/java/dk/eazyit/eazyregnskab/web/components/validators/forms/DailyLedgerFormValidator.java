package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author
 */
public class DailyLedgerFormValidator extends BaseFormValidator {

    private static final long serialVersionUID = 1L;

    public DailyLedgerFormValidator(FormComponent... components) {
        super(components);
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> formComponent1 = getDependentFormComponents()[0];
        DailyLedger dailyLedger = (DailyLedger) formComponent1.getForm().getModelObject();


        if (dailyLedger.getId() == 0 && financeAccountService.findDailyLedgerByLegalEntityAndName(getCurrentLegalEntity(),
                formComponent1.getInput()) != null) {
            error(formComponent1, "name.all.ready.exists");
        }
        if (formComponent1.getInput().length() > 20) {
            error(formComponent1, "name.to.long");
        }
    }
}
