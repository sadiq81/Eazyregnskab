package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public class FinanceAccountTypeDropDownChoice extends EnumDropDownChoice<FinanceAccountType> {

    public FinanceAccountTypeDropDownChoice(String id) {
        super(id);
    }

    public FinanceAccountTypeDropDownChoice(String id, IModel<FinanceAccountType> model) {
        super(id, model);
    }

    public FinanceAccountTypeDropDownChoice(String id, List<FinanceAccountType> choices) {
        super(id, choices);
    }

    public FinanceAccountTypeDropDownChoice(String id, List<FinanceAccountType> choices, EnumChoiceRenderer<FinanceAccountType> choiceRenderer) {
        super(id, choices, choiceRenderer);
    }

    public FinanceAccountTypeDropDownChoice(String id, IModel<FinanceAccountType> model, EnumChoiceRenderer<FinanceAccountType> choiceRenderer) {
        super(id, model, choiceRenderer);
    }

    public FinanceAccountTypeDropDownChoice(String id, IModel<FinanceAccountType> model, List<FinanceAccountType> choices, EnumChoiceRenderer<FinanceAccountType> renderer) {
        super(id, model, choices, renderer);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        if (((FinanceAccount) getForm().getModelObject()).isSystemAccount()) {
            addChoice(FinanceAccountType.getSystemAccounts());
        } else {
            removeChoice(FinanceAccountType.getSystemAccounts());
        }
    }
}
