package dk.eazyit.eazyregnskab.web.components.validators.forms;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author
 */
public class FinanceAccountFormValidator extends BaseFormValidator {

    private static final long serialVersionUID = 1L;

    public FinanceAccountFormValidator(FormComponent... components) {
        super(components);
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<Integer> formComponent1 = (FormComponent<Integer>) getDependentFormComponents()[0];
        Integer accountNumber = formComponent1.getConvertedInput();

        if (financeAccountService.findFinanceAccountByLegalEntityAndName(getCurrentLegalEntity(), accountNumber) != null) {
            error(formComponent1, "number.all.ready.exists");
        }
        if (formComponent1.getInput().length() > 20) {
            error(formComponent1, "number.to.long");
        }
    }
}
