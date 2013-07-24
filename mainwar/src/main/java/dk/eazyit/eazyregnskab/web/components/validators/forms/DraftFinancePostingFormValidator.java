package dk.eazyit.eazyregnskab.web.components.validators.forms;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

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
        final FormComponent<?> vat = getDependentFormComponents()[3];
        final FormComponent<?> reverseVat= getDependentFormComponents()[4];


        //TODO Check date within accounting year
        if (text.getInput().length() > 50) {
            error(new NotificationMessage(new ResourceModel("text.to.long")).hideAfter(Duration.seconds(DURATION)));
        }
        if (financeAccount.getConvertedInput() == null && reverseFinanceAccount.getConvertedInput() == null) {
            error(new NotificationMessage(new ResourceModel("must.chose.one.finance.account")).hideAfter(Duration.seconds(DURATION)));
        }
        if (financeAccount.getConvertedInput() == null && vat.getConvertedInput() != null) {
            error(new NotificationMessage(new ResourceModel("must.choose.account.for.vat")).hideAfter(Duration.seconds(DURATION)));
        }
        if (reverseFinanceAccount.getConvertedInput() == null && reverseVat.getConvertedInput() != null) {
            error(new NotificationMessage(new ResourceModel("must.choose.account.for.vat")).hideAfter(Duration.seconds(DURATION)));
        }

    }
}
