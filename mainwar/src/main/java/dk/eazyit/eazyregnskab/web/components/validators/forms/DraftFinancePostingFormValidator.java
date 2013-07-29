package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author
 */
public class DraftFinancePostingFormValidator extends BaseFormValidator {

    private static final long serialVersionUID = 1L;

    public DraftFinancePostingFormValidator(FormComponent... components) {
        super(components);
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> text = getDependentFormComponents()[0];
        final FormComponent<?> financeAccount = getDependentFormComponents()[1];
        final FinanceAccount account = (FinanceAccount) getDependentFormComponents()[1].getModelObject();
        final FormComponent<?> reverseFinanceAccount = getDependentFormComponents()[2];
        final FinanceAccount reverseAccount = (FinanceAccount) getDependentFormComponents()[2].getModelObject();
        final FormComponent<?> vat = getDependentFormComponents()[3];
        final FormComponent<?> reverseVat = getDependentFormComponents()[4];


        //TODO Check date within accounting year
        if (text.getInput().length() > 50) {
            error(text, "text.to.long");
        }
        if (financeAccount.getConvertedInput() == null && reverseFinanceAccount.getConvertedInput() == null) {
            error(financeAccount, "must.chose.one.finance.account");
        }
        if (financeAccount.getConvertedInput() == null && vat.getConvertedInput() != null) {
            error(financeAccount, "must.choose.account.for.vat");
        }
        if (reverseFinanceAccount.getConvertedInput() == null && reverseVat.getConvertedInput() != null) {
            error(reverseFinanceAccount, "must.choose.account.for.vat");
        }
        if (account != null && account.isLocked()) {
            error(reverseFinanceAccount, "account.is.locked");
        }
        if (reverseAccount != null && reverseAccount.isLocked()) {
            error(reverseFinanceAccount, "reverse.account.is.locked");
        }

    }
}
