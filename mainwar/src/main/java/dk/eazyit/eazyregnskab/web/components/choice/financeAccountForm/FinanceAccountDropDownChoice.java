package dk.eazyit.eazyregnskab.web.components.choice.financeAccountForm;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

import java.util.List;

/**
 * @author
 */
public class FinanceAccountDropDownChoice<T> extends DropDownChoice<T> {

    public FinanceAccountDropDownChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        setRequired(isSumAccount());
                       setVisibilityAllowed(isSumAccount());
    }

    public boolean isSumAccount() {
        return ((FinanceAccount)getForm().getModelObject()).getFinanceAccountType() == FinanceAccountType.SUM;
    }
}
