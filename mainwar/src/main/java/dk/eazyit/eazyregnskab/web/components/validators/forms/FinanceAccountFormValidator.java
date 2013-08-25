package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
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
        FinanceAccount account = (FinanceAccount) form.getModelObject();
        Integer accountNumber = formComponent1.getConvertedInput();

        FinanceAccount number = financeAccountService.findFinanceAccountByLegalEntityAndName(getCurrentLegalEntity(), accountNumber);

        if (number != null && !account.getId().equals(number.getId())) {
            error(formComponent1, "ChartOfAccountsPage.number.all.ready.exists");
        }

        if (formComponent1.getInput().length() > 20) {
            error(formComponent1, "ChartOfAccountsPage.number.to.long");
        }
    }
}
