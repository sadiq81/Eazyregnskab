package dk.eazyit.eazyregnskab.web.components.validators.forms;

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
        final FormComponent<?> reverseFinanceAccount = getDependentFormComponents()[2];


        //TODO Check date within accounting year


        if (text.getInput().length() > 50) {
            error(text, "text.to.long");
        }
        if (financeAccount.getConvertedInput() == null && reverseFinanceAccount.getConvertedInput() == null) {
            error(financeAccount, "must.chose.one.finance.account");
        }


    }
}
